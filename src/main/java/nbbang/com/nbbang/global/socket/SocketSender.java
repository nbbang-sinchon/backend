package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanResponseDto;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.socket.SocketSendDto;
import org.springframework.beans.factory.annotation.Autowired;
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
        send(CHATTING, partyId, chatSendResponseDto);
    }
    public void sendChatting(Long partyId, Object data){
        send(CHATTING, partyId, data);
    }
    public void sendBreadBoard(Long partyId, Object data){
        send(BREAD_BOARD, partyId, data);
    }
    public void sendGloabl(Long memberId, Object data){
        send(GLOBAL, memberId, data);
    }
    public void send(String destination, Long id, Object data){
        SocketSendDto socketSendDto = SocketSendDto.createSocketSendDto(data);
        simpMessagingTemplate.convertAndSend("/topic/"+destination+"/" + id, socketSendDto);
    }
}
