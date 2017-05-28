package sample.websocket;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import sample.Lobby;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by artem on 5/26/17.
 */
@Service
public class GameHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Autowired
    Lobby lobby;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        super.beforeHandshake(request, response, wsHandler, attributes);
        final HttpSession session = getSessionShadowed(request);

        if (session != null) {
            final String email = (String) session.getAttribute(WSDict.SESSION_ATTRIBUTE);
            final Integer partyId = lobby.addPlayer(email);
            final Integer playerId = lobby.getPlayerPartyId(email, partyId);

            attributes.put(WSDict.PARTY_ID, partyId);
            attributes.put(WSDict.PLAYER_ID, playerId);

            return true;
        }

        return true;   // TODO refuse connection if not logged in
    }

    @Nullable
    private HttpSession getSessionShadowed(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            final ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(isCreateSession());
        }
        return null;
    }
}
