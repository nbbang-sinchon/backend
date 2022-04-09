package nbbang.com.nbbang.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.*;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.socket.service.ChatRoomService;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "ChatRoom", description = "채팅방 api")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;
    private final PartyService partyService;
    private final CurrentMember currentMember;
    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 조회", description = "채팅방을 파티 id 로 조회합니다. ")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @GetMapping("/{party-id}")
    public DefaultResponse select(@PathVariable("party-id") Long partyId, @RequestParam(required = false) Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        chatRoomService.readMessage(partyId, currentMember.id(), false);
        Party party = partyService.findById(partyId);
        Page<Message> messages = chatService.findMessages(party, currentMember.id(), PageRequest.of(0, pageSize));
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.READ_CHAT, ChatResponseDto.createByPartyAndMessagesEntity(party, messages.getContent(), currentMember.id()));
    }

    @Operation(summary = "채팅 메시지 조회", description = "페이징이 적용된 채팅 메시지를 조회합니다. 쿼리 파라미터로 커서 id 를 전송하면 쿼리에 커서 페이징이 적용됩니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @GetMapping("/{party-id}/messages")
    public DefaultResponse selectChatMessages(@PathVariable("party-id") Long partyId, @ParameterObject PageableDto pageableDto, @RequestParam(required = false) Long cursorId) {
        Party party = partyService.findById(partyId);
        Page<Message> messages = chatService.findMessagesByCursorId(party, currentMember.id(), pageableDto.createPageRequest(), cursorId);
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.READ_CHAT, ChatSendListResponseDto.createByEntity(messages.getContent(), currentMember.id()));
    }

}
