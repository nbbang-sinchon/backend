package nbbang.com.nbbang.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 방식
public class StompWebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

    //end point, message broker 걸어줘야함

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/stompTest").setAllowedOrigins("*").withSockJS();
        //stomp는 sockJS 기반으로 돌기 때문에 마지막에 꼭 붙여줘야함.
        // var sock = new SockJS("/stompTest") 처럼 end point로 소켓을 연결함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/topic"); //토픽, 큐 방식이 있는데 보통 토픽으로함
        registry.setApplicationDestinationPrefixes("/");
        // client.send('/TTT', {}, ) 컨트롤러에 요청함! /TTT로. 컨트롤러가 받음. 프론트에서 받을때는 토픽을 구독
/*        컨트롤러 코드는
          @MessageMapping("/TTT")
          @SendTo("/topic/message")<- 여기 보내줌
          public String tttMessage(string message)
*
* */

    }
}