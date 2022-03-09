package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static nbbang.com.nbbang.global.socket.SocketDestination.*;

@Component
@RequiredArgsConstructor
public class SocketSender {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CurrentMember currentMember;
    public void sendChattingByMessage(Message message){
        ChatSendResponseDto chatSendResponseDto = ChatSendResponseDto.createByMessage(message, currentMember.id());
        Long partyId = message.getParty().getId();
        send(CHATTING, partyId,  chatSendResponseDto);
    }
    public void sendChattingReadMessage(Long partyId, Object data){
        send(CHATTING, partyId, "reading", data);
    }
    public void sendBreadBoard(Long partyId, Object data){
        send(BREAD_BOARD, partyId, data);
    }
    public void sendGlobal(Long memberId, Object data){
        send(GLOBAL, memberId, data);
    }

    public void send(String destination, Long id, Object data){
        send(destination,id, destination, data );
    }

    public void send(String destination, Long id, String type, Object data){
        SocketSendDto socketSendDto = SocketSendDto.createSocketSendDto(type, data);
        simpMessagingTemplate.convertAndSend("/topic/"+destination+"/" + id, socketSendDto);
    }
}
