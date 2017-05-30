package sample.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameLogic.event_system.messages.PlatformState;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sample.services.account.AccountServiceDB;

import javax.naming.AuthenticationException;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by sergey on 22.04.17.
 */

public class GameWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = Logger.getLogger(GameWebSocketHandler.class.getName());

    @NotNull
    private AccountServiceDB accountServiceDB;
    @NotNull
    private final GameSocketService gameSocketService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameWebSocketHandler(@NotNull AccountServiceDB accountServiceDB, @NotNull GameSocketService gameSocketService) {
        this.accountServiceDB = accountServiceDB;
        this.gameSocketService = gameSocketService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.info("Connection established");

        String email = (String) webSocketSession.getAttributes().get(WSDict.SESSION_ATTRIBUTE);
        if (email == null) {
            email = RandomUtils.getRandomString();
            LOGGER.info("Random email generated");
        }

        gameSocketService.registerUser(email, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        final int partyId = (int) session.getAttributes().get(WSDict.PARTY_ID);
        final int playerId = (int) session.getAttributes().get(WSDict.PLAYER_ID);
        handleMessage(partyId, playerId, message);
    }


    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(int partyId, int playerId, TextMessage text) {
        System.out.println("Handle message");
        try {
            final ObjectNode message = objectMapper.readValue(text.getPayload(), ObjectNode.class);
            if (message.get("type").asText().equals(WSDict.PLATFORM_MOVED)) {
                gameSocketService.updatePlatformState(
                        partyId,
                        playerId,
                        objectMapper.readValue(message.get("data").toString(), PlatformState.class)  // TODO make something more elegant
                );
            }

        } catch (IOException ex) {
            System.out.println("wrong json format" + ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("Websocket transport problem");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        LOGGER.info("Connection closed");

        final int partyId = (int) webSocketSession.getAttributes().get(WSDict.PARTY_ID);
        final int playerId = (int) webSocketSession.getAttributes().get(WSDict.PLAYER_ID);
        final String email = gameSocketService.removeUser(partyId, playerId);

        if (email != null) {
            LOGGER.info("Player " + email + " was successfully removed from the game");
        } else {
            LOGGER.info("Nobody to remove");
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
