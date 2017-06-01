package sample.websocket;

import org.jetbrains.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;


@Service
public class GameHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        super.beforeHandshake(request, response, wsHandler, attributes);
        final HttpSession session = getSessionShadowed(request);

        if (session != null) {
            final String email = (String) session.getAttribute(WSDict.SESSION_ATTRIBUTE);

            if (email != null) {
                attributes.put(WSDict.SESSION_ATTRIBUTE, email);
            }

            attributes.put(WSDict.SESSION_ID,session.getId());
        }

        return true;
    }

    @Nullable
    private HttpSession getSessionShadowed(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            final ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession();
            //return serverRequest.getServletRequest().getSession(isCreateSession());
        }
        return null;
    }
}
