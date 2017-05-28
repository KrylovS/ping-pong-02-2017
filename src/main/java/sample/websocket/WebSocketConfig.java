package sample.websocket;

/**
 * Created by sergey on 22.04.17.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import sample.services.account.AccountServiceDB;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    AccountServiceDB accountServiceDB;
    @Autowired
    GameSocketService gameSocketService;
    @Autowired
    GameHandshakeInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GameWebSocketHandler(accountServiceDB, gameSocketService), "/api/user/game")
                .addInterceptors(interceptor)
                .setAllowedOrigins("*");
    }
}
