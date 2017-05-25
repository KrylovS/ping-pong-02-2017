package sample.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.event_system.messages.PlatformState;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Lobby;
import sample.services.game.GameService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;
import java.util.stream.IntStream;


/**
 * Created by sergey on 22.04.17.
 */
@Service
public class GameSocketService
{
    private final Logger logger = Logger.getLogger(GameSocketService.class.getName());
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    //private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private Lobby lobby;
    @Autowired
    private GameService gameService;

    public void registerUser(@NotNull String email, @NotNull WebSocketSession webSocketSession) {
        sessions.put(email, webSocketSession);
    }

    public boolean isConnected(@NotNull String email) {
        return sessions.containsKey(email) && sessions.get(email).isOpen();
    }

    public void removeUser(@NotNull String email)
    {
        sessions.remove(email);
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

    public void addUserToGame(HttpSession httpSession, @NotNull String email) {
        final Integer partyId = lobby.addPlayer(email);
        final Integer playerId = lobby.getPlayerPartyId(email, partyId);
        httpSession.setAttribute("partyId", partyId);
        httpSession.setAttribute("playerId", playerId);
    }

    public void recievePlayerState(String email, Message<?> message) {
        final WebSocketSession webSocketSession = sessions.get(email);

        final PlatformState platformState = (PlatformState) message.getContent();
        final Integer partyId = (Integer) webSocketSession.getAttributes().get("partyId");
        final Integer playerId = (Integer) webSocketSession.getAttributes().get("playerId");

        gameService.addUserTask(partyId, playerId, platformState);
    }

    public void updateGamePartyState(Integer partyId, List<GameWorldState> gameWorldStates) {
        final List<String> players = lobby.getSortedPlayers(partyId);

        IntStream.range(0, GameConfig.PLAYERS_NUM).forEach(i -> {
            try {
                sendMessageToUser(players.get(i), new Message<>("state", gameWorldStates.get(i)));
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        });
    }
}
