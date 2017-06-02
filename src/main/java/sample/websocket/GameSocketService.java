package sample.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gameLogic.common.CommonFunctions;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.*;
import gameLogic.game.EventBus;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Lobby;
import sample.services.game.GameService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class GameSocketService
{
    private final Logger logger = Logger.getLogger(GameSocketService.class.getName());
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private Lobby lobby;
    @Autowired
    private GameService gameService;

    public GameSocketService() {
        setListeners();
    }

    public void setListeners() {
        EventBus.addEventListener(
                SectorCollision.class.getName(),
                event -> {
                    final SectorCollision realEvent = (SectorCollision) event;
                    this.handleUserSectorCollision(realEvent.getGameId(), realEvent.getUserIndex(), realEvent.isVictory());
                    return null;
                }
        );
    }

    public void registerUser(@NotNull String email, @NotNull WebSocketSession webSocketSession) {
        sessions.put(email, webSocketSession);

        final int partyId = lobby.addPlayer(email);
        final int playerId = lobby.getPlayerPartyId(email, partyId);
        webSocketSession.getAttributes().put(WSDict.PARTY_ID, partyId);
        webSocketSession.getAttributes().put(WSDict.PLAYER_ID, playerId);

        final List<Message<List<PlayerAnnouncement>>> lobbyState = getLobbyState(partyId);
        transmitTransformedMessage(partyId, lobbyState);

        if (lobbyState.size() == GameConfig.PLAYERS_NUM) {
            transmitGetReadyMessage(partyId, lobbyState);
            transmitGameStartMessage(partyId);
        }
    }

    public boolean isConnected(@NotNull String email) {
        return sessions.containsKey(email) && sessions.get(email).isOpen();
    }

    @Nullable
    public String removeUser(@NotNull int partyId, @NotNull int playerId) {
        final String email = lobby.removePlayer(partyId, playerId);
        if (email != null) {
            sessions.remove(email);
        }
        return email;
    }

    public void cutDownConnection(@NotNull String email, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(email);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(@NotNull String email, @NotNull Message<?> message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(email);
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + email);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        try {
            //noinspection ConstantConditions
            webSocketSession.sendMessage(new TextMessage(message.getStingMessage()));
        } catch (JsonProcessingException | WebSocketException e) {
            throw new IOException("Unable to send message", e);
        }
    }

    public void setPlatformState(int partyId, int playerId, PlatformState platformState) {
        gameService.addPlatformSetTask(partyId, playerId, platformState);
    }

    public void updatePlatformState(int partyId, int playerId, PlatformStateUpdate platformStateUpdate) {
        gameService.addPlatformUpdateTask(partyId, playerId, platformStateUpdate);
    }

    public List<Message<List<PlayerAnnouncement>>> getLobbyState(Integer partyId) {
        final List<PlayerAnnouncement> lobbyState = lobby.getCurrLobbyState(partyId);

        return IntStream.range(0, lobbyState.size()).boxed()
                .map(i -> lobbyState.stream()
                        .map(pa -> pa.getDiscreteRotation(-i, GameConfig.PLAYERS_NUM))
                        .sorted(Comparator.comparingInt(PlayerAnnouncement::getPosition))
                        .collect(Collectors.toList())
                )
                .map(messageContent -> new Message<>(WSDict.ANNOUNCE, messageContent))
                .collect(Collectors.toList());
    }

    public void updateGamePartyState(Integer partyId, List<Message<GameWorldState>> messageList) {
        transmitTransformedMessage(
                partyId,
                messageList
        );
    }

    public void transmitGetReadyMessage(Integer partyId, List<Message<List<PlayerAnnouncement>>> lobbyState) {
        transmitSameMessage(partyId, new Message<>(WSDict.GET_READY, lobbyState));
    }

    public void transmitGameStartMessage(Integer partyId) {
        transmitSameMessage(partyId, new Message<>(WSDict.START_GAME, ""));
    }

    private void handleUserSectorCollision(int gameId, int userIndex, boolean isSuccess) {
        final List<Message<GameTermination>> messages = IntStream.range(0, GameConfig.PLAYERS_NUM).boxed()
                .map(i -> {
                    final GameTermination gameTermination = new GameTermination(
                            isSuccess,
                            CommonFunctions.getCircularOffset(userIndex, -i, GameConfig.PLAYERS_NUM),
                            System.currentTimeMillis()
                    );
                    return new Message<>(WSDict.GAME_TERMINATION, gameTermination);
                })
                .collect(Collectors.toList());

        transmitTransformedMessage(gameId, messages);
    }

    private <T> void transmitSameMessage(Integer partyId, Message<T> message) {
        lobby.getSortedPlayers(partyId).forEach(player -> {
            try {
                sendMessageToUser(player, message);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        });
    }

    private <T> void transmitTransformedMessage(Integer partyId, List<Message<T>> messages) {
        final List<String> players = lobby.getSortedPlayers(partyId);

        IntStream.range(0, players.size()).forEach(i -> {
            try {
                sendMessageToUser(players.get(i), messages.get(i));
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        });
    }
}
