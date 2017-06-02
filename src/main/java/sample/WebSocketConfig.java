package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import sample.services.account.AccountServiceDB;
import sample.services.score.ScoreService;
import sample.websocket.GameHandshakeInterceptor;
import sample.websocket.GameSocketService;
import sample.websocket.GameWebSocketHandler;

@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    AccountServiceDB accountServiceDB;
    @Autowired
    GameSocketService gameSocketService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    GameHandshakeInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GameWebSocketHandler(accountServiceDB, gameSocketService, scoreService), "/api/user/game")
                .addInterceptors(interceptor)
                .setAllowedOrigins("*");
    }
}
