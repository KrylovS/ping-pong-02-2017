package sample.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by sergey on 22.04.17.
 */
@Service
public class GameSocketService
{
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    //private ObjectMapper objectMapper = new ObjectMapper();

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

    public void sendMessageToUser(@NotNull String email, @NotNull Message message) throws IOException {
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
            throw new IOException("Unnable to send message", e);
        }
    }
}
