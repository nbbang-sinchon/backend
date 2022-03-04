package nbbang.com.nbbang.global.response;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.socket.SocketSendDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketSender {
    private final SimpMessagingTemplate simpMessagingTemplate;
    public void sendChatting(Long partyId, Object data){
        send("chatting", partyId, data);
    }
    public void sendBreadBoard(Long partyId, Object data){
        send("breadBoard", partyId, data);
    }
    public void sendGloabl(Long memberId, Object data){
        send("global", memberId, data);
    }
    public void send(String destination, Long id, Object data){
        SocketSendDto socketSendDto = SocketSendDto.createSocketSendDto(data);
        simpMessagingTemplate.convertAndSend("/topic/"+destination+"/" + id, socketSendDto);
    }

}
