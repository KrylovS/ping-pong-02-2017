package sample.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sample.services.account.AccountServiceDB;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;

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
        final String email = "random"; //final String email = (String) session.getAttributes().get(SESSIONATRIBUTE);
        System.out.println("Handle text message");
        /*if (email == null || (accountServiceDB.getUser(email) == null)) {
            throw new AuthenticationException("Only authenticated users allowed to play a game");
        }*/
        handleMessage(email, message);
    }


    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(String email, TextMessage text) {
        System.out.println("Handle message");
        try {
            final Message message = objectMapper.readValue(text.getPayload(), Message.class);
            gameSocketService.sendMessageToUser(email, message);
        } catch (IOException ex) {
            System.out.println("wrong json format at ping response" + ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("Websocket transport problem");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("Connection closed");
        final String email = "random"; //final String email = (String) webSocketSession.getAttributes().get(SESSIONATRIBUTE);
        /*if (email == null) {
            System.out.println("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }*/
        gameSocketService.removeUser(email);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
