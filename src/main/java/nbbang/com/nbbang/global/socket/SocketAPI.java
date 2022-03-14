package nbbang.com.nbbang.global.socket;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanResponseDto;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatAlarmResponseDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendRequestDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nbbang.com.nbbang.global.socket.SocketDestination.*;

@Tag(name = "SocketDevelop", description = "소켓 관련된 API입니다.")
@NoArgsConstructor(access = AccessLevel.PRIVATE) // ONLY FOR SWAGGER. Don't do anything for Logic.
@RestController
@RequestMapping("socket/develop")
public class SocketAPI {
    @Autowired
    MessageService messageService;
    @Autowired
    CurrentMember currentMember;
    @Autowired
    PartyService partyService;

    @Operation(summary = "[CHATTING] 채팅 메시지 전송", description = "다른 사람이 채팅 메시지를 전송하면, 채팅방 내의 유저들이 받는 소켓입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatSendResponseDto.class)))
    @PostMapping("/chatting")
    public SocketSendDto socketEmitApi(@RequestBody ChatSendRequestDto chatSendRequestDto){
        Message message = messageService.send(1L, 1L, chatSendRequestDto.getContent());
        ChatSendResponseDto data = ChatSendResponseDto.createByMessage(message);
        return SocketSendDto.builder().type(CHATTING).data(data).build();
    }

    @Operation(summary = "[CHATTING] 채팅 읽음", description = "다른 사람이 채팅방에 들어와 메시지를 읽으면, 채팅방 내의 유저들이 받는 소켓입니다. lastReadMessageId보다 큰 id를 갖는 메시지들은 전부 읽지 않은 사람 수를 -1 해주세요.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatReadSocketDto.class)))
    @PostMapping("/chatting/reading")
    public SocketSendDto readMessage(){
        Long lastReadMessageId = 5L;
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        return SocketSendDto.builder().type("reading").data(chatReadSocketDto).build();
    }

    @Operation(summary = "[BREAD-BOARD] 빵판", description = "빵판과 관련된 소켓입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BbangpanResponseDto.class)))
    @PostMapping("/bread-board")
    public SocketSendDto breadBoard(){
        Party party = partyService.findById(1L);
        BbangpanResponseDto bbangpanResponseDto = BbangpanResponseDto.createDtoByParty(party);
        return SocketSendDto.builder().type(BREAD_BOARD).data(bbangpanResponseDto).build();

    }

    @Operation(summary = "[GLOBAL] 알람", description = "참가중인 파티에 새로운 채팅이 들어오면 알람을 보냅니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatAlarmResponseDto.class)))
    @PostMapping("/global")
    public SocketSendDto global(){
        Message message = messageService.send(1L, 2L, "다른 사람이 보낸 채팅");
        ChatSendResponseDto data = ChatSendResponseDto.createByMessage(message);
        return SocketSendDto.builder().type(GLOBAL).data(ChatAlarmResponseDto.create(Party.builder().id(0L).title("test party").build(), data)).build();
    }

}
