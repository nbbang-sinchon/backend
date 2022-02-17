package nbbang.com.nbbang.domain.chat.controller;

import nbbang.com.nbbang.domain.chat.dto.MessageVo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatSocketController {
    @MessageMapping("/receive")
    @SendTo("/send")
    public MessageVo SocketHandler(MessageVo vo) {
        return vo;
    }

}
