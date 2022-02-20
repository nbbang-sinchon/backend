package nbbang.com.nbbang.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {
    @MessageMapping("/TTT")
    @SendTo("topic/message")
    public String ttt(String message) throws Exception{
        // JSON으로 받을수있음
        return message; // 토픽에 publshing
    }
}
