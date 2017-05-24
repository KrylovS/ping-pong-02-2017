package sample.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import gameLogic.event_system.messages.GameWorldState;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sample.Lobbi;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private final Lobbi lobbi = new Lobbi();

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
            throw new IOException("Unnable to send message", e);
        }
    }

    public void addUserInGame(HttpSession httpSession, @NotNull String email) {
        final Integer partyId = lobbi.addPlayerAndReturnPartyId(email);
        final Integer playerId = lobbi.getPlayerIdInParty(email, partyId);
        httpSession.setAttribute("partyId", partyId);
        httpSession.setAttribute("playerId", playerId);
    }

    public void recievePlayerState(String email, Message<?> message) {
        final WebSocketSession webSocketSession = sessions.get(email);
        final Integer partyId = (Integer) webSocketSession.getAttributes().get("partyId");
        final Integer playerId = (Integer) webSocketSession.getAttributes().get("playerId");

    }

    public void updateGamePartyState(Integer partyId, List<GameWorldState> gameWorldStates) throws IOException {
        Message<List<GameWorldState>> message = new Message<>("state", gameWorldStates);
        List <String> playersList = new ArrayList<>();
        for (String player : playersList) {
            sendMessageToUser(player, message);
        }

    }
}
