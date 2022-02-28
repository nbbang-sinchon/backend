package nbbang.com.nbbang.global.config;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.handler.StompHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 방식
@RequiredArgsConstructor
public class StompWebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    //end point, message broker 걸어줘야함
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
        // var sock = new SockJS("/chat") 처럼 end point로 소켓을 연결함
        //stomp는 sockJS 기반으로 돌기 때문에 마지막에 꼭 붙여줘야함.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/topic");
        registry.enableSimpleBroker("/global");
        registry.setApplicationDestinationPrefixes("/");
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}