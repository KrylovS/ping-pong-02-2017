package sample.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameLogic.event_system.messages.PlatformState;
import gameLogic.event_system.messages.PlatformStateUpdate;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sample.UserProfile;
import sample.services.account.AccountServiceDB;
import sample.services.score.ScoreService;

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
    @NotNull
    private final ScoreService scoreService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameWebSocketHandler(@NotNull AccountServiceDB accountServiceDB, @NotNull GameSocketService gameSocketService,
                                @NotNull ScoreService scoreService) {
        this.accountServiceDB = accountServiceDB;
        this.gameSocketService = gameSocketService;
        this.scoreService = scoreService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.info("Connection established");

        String email = (String) webSocketSession.getAttributes().get(WSDict.SESSION_ATTRIBUTE);
        if (email == null) {
            email = RandomUtils.getRandomString();
            LOGGER.info("Random email generated");
        }

        final String sessionId = (String) webSocketSession.getAttributes().get(WSDict.SESSION_ID);
        scoreService.addSession(email, sessionId);

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
//        System.out.println("Handle message");
        try {
            final ObjectNode message = objectMapper.readValue(text.getPayload(), ObjectNode.class);
            final String messageType = message.get("type").asText();

            if (messageType.equals(WSDict.PLATFORM_MOVED)) {
                gameSocketService.setPlatformState(
                        partyId,
                        playerId,
                        objectMapper.readValue(message.get("data").toString(), PlatformState.class)
                );
            } else if (messageType.equals(WSDict.PLATFORM_UPDATE)) {
                gameSocketService.updatePlatformState(
                        partyId,
                        playerId,
                        objectMapper.readValue(message.get("data").toString(), PlatformStateUpdate.class)
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

        //final int score = (int) webSocketSession.getAttributes().get(WSDict.SCORE);

        final String email = gameSocketService.removeUser(partyId, playerId);

        if (email != null) {
            final int score = 1; // stub
            final UserProfile userProfile = accountServiceDB.getUser(email);
            if (userProfile != null) {
                userProfile.setScore(score);
                accountServiceDB.updateScore(userProfile);
            } else {
                scoreService.addScore(email, score);
            }
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
