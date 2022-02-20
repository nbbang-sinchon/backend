package nbbang.com.nbbang.global.config;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.handler.ChatHandler;
import nbbang.com.nbbang.global.handler.ReplyEchoHandler;
import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.*;


// handler를 등록하는 곳
// https://dev-gorany.tistory.com/212
@Configuration // 설정이다.
@EnableWebSocket // web socket을 가능하게 함
@RequiredArgsConstructor
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer { // WebSocketMessageBrokerConfigurer

    private final ChatHandler chatHandler;
    private final ReplyEchoHandler replyEchoHandler;
    // private final static String CHAT_ENDPOINT = "/chat";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 도메인이 다른 서버에서도 접속 가능하도록 CORS : setAllowedOrigins("*"); 를 추가해준다.
        // 이제 클라이언트가 ws://localhost:8080/chat으로 커넥션을 연결하고 메세지 통신을 할 수 있는 준비를 마쳤다.
        registry.addHandler(chatHandler, "ws/chat").setAllowedOrigins("*");
        registry.addHandler(replyEchoHandler, "/replyEcho").setAllowedOrigins("*");
        // 소켓 서버의 domain과 사이트의 ref가 달라서 어뷰징일 수 있음.
        // 다른 사이트에서 소켓 쏘는 것을 방지
        // 어디서 오는지 알고 거기만 걸어놔야함.
    }

/*
@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/send");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/").setAllowedOrigins("*").withSockJS();
    }

 */
}
