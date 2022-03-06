
package nbbang.com.nbbang.global.socket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.dto.ChatResponseDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.chat.service.ChatSessionService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import nbbang.com.nbbang.global.security.jwt.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// https://daddyprogrammer.org/post/5290/spring-websocket-chatting-server-enter-qut-event-view-user-count/
// https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
@Slf4j
@Component
//@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {


    private final ChatService chatService;
    private final PartyService partyService;
    private final ChatSessionService chatSessionService;

    public StompHandler(ChatService chatService, PartyService partyService, ChatSessionService chatSessionService,
                        PartyMemberService partyMemberService, JwtService jwtService, @Lazy SocketSender socketSender) {
        this.chatService = chatService;
        this.partyService = partyService;
        this.chatSessionService = chatSessionService;
        this.partyMemberService = partyMemberService;
        this.jwtService = jwtService;
        this.socketSender = socketSender;
    }

    private final PartyMemberService partyMemberService;
    private final JwtService jwtService;
    private final SocketSender socketSender;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("CONNECT message = {}",message);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("SUBSCRIBED message = {}",message);
            String destination = accessor.getDestination();
            if(destination.startsWith("/topic/global"))  {
                Long memberId = Long.valueOf(destination.substring(14));
                log.info("SUBSCRIBED memberId: {}", memberId);
            }
            else if(destination.startsWith("/topic/chatting")){
                Long partyId = Long.valueOf(destination.substring(16));
                enterChatRoom(accessor, partyId);
                log.info("SUBSCRIBED chat RoomId: {}", partyId);
            }else if(destination.startsWith("/topic/breadBoard")){
                Long partyId = Long.valueOf(destination.substring(18));
                log.info("SUBSCRIBED breadBoard RoomId: {}", partyId);
            }
            else{
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요.");
            }
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            log.info("UNSUBSCRIBE message = {}",message);
             Long partyId = exitChatRoomIfExist(accessor);
             log.info("UNSUBSCRIBE RoomId: {}", partyId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            log.info("DISCONNECT message = {}",message);
             Long partyId = exitChatRoomIfExist(accessor);
             log.info("DISCONNECT RoomId: {}", partyId);
        }
        return message;
    }

    private void enterChatRoom(StompHeaderAccessor accessor, Long partyId){
        String sessionId = accessor.getSessionId();
        chatSessionService.createBySessionIdAndPartyId(sessionId, partyId);
        readMessage(partyId, 1L);
        partyService.updateActiveNumber(partyId, 1);
    }

    private void readMessage(Long partyId, Long memberId) {
        Long lastReadMessageId = chatService.readMessage(partyId, memberId);
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        socketSender.sendChatting(partyId, chatReadSocketDto);
    }

    private Long exitChatRoomIfExist(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        Long partyId =  chatSessionService.deleteIfExistBySessionId(sessionId);
        if (partyId!=-1) {
            partyService.updateActiveNumber(partyId, -1);
            //************* memberId 넣는 로직 추가해야함 *************
            chatService.exitChatRoom(partyId, 1L);
        }
        return partyId;
    }
}
