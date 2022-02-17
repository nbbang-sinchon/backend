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
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.dto.CursorPageableDto;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.exception.ErrorResponse;
import nbbang.com.nbbang.global.exception.PartyId;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import nbbang.com.nbbang.global.support.FileUpload.FileUploadService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Tag(name = "Chat", description = "채팅방 api (로그인 구현시 올바른 토큰을 보내지 않을 경우 401 Unauthorized 메시지를 받습니다.).")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final FileUploadService fileUploadService;
    private final ChatService chatService;
    private final PartyService partyService;
    private final PartyRepository partyRepository;
    private Long memberId = 1L; // 로그인 기능 구현시 삭제 예정

    @Operation(summary = "채팅방 조회", description = "채팅방을 파티 id 로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @GetMapping("/{party-id}")
    public DefaultResponse select(@PathVariable("party-id") Long partyId, @RequestParam(required = false) Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        Party party = partyRepository.findById(partyId).get(); // Party Service 구현 시 바꿔야 할 것 같습니다.
        Long lastMessageId = chatService.findLastMessageId(partyId);
        //Page<Message> messages = chatService.findMessages(partyId, pageableDto.createPageRequest(), lastMessageId);
        Page<Message> messages = chatService.findMessagesByCursorId(partyId, PageRequest.of(0, pageSize), lastMessageId);
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.READ_CHAT, ChatResponseDto.createByPartyEntity(party, lastMessageId, messages.getContent()));
    }

    @Operation(summary = "채팅 메시지 조회", description = "커서 페이징이 적용된 채팅 메시지를 조회합니다. 클라이언트는 커서 id 를 쿼리 파라미터에 전송해야 합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @GetMapping("/{party-id}/messages")
    public DefaultResponse selectChatMessages(@PathVariable("party-id") Long partyId, @ParameterObject CursorPageableDto cursorPageableDto) {
        Page<Message> messages = chatService.findMessagesByCursorId(partyId, cursorPageableDto.createPageRequest(), cursorPageableDto.getCursorId());
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.READ_CHAT, ChatMessageListResponseDto.createByEntity(messages.getContent()));
    }

    @Operation(summary = "채팅 메시지 전송", description = "채팅 메시지를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/messages")
    public DefaultResponse sendMessage(@PathVariable("party-id") Long partyId, @RequestBody ChatMessageSendRequestDto chatMessageSendRequestDto) {
        chatService.sendMessage(memberId, partyId, chatMessageSendRequestDto.getContent(), LocalDateTime.now());
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPLOADED_MESSAGE);
    }

    @Operation(summary = "메시지 사진 업로드", description = "메시지 사진을 업로드합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatMessageImageUploadResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @PostMapping(path = "/{party-id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefaultResponse sendImage(@PathVariable("party-id") Long partyId,
                                                       @Schema(description = "이미지 파일을 업로드합니다.")
                                                       @RequestPart MultipartFile imgFile) {
        String filePath = fileUploadService.fileUpload(imgFile);
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPLOADED_MESSAGE, new ChatMessageImageUploadResponseDto(filePath));
    }

    @Operation(summary = "채팅방에서 나가기", description = "채팅방에서 나갑니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/out")
    public DefaultResponse exitChat(@PathVariable("party-id") Long partyId) {
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.EXIT_CHAT);
    }

    @Operation(summary = "채팅방 상태 변경", description = "방장만 채팅방 속성을 변경할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. Status 를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}/status")
    public DefaultResponse changeStatus(@PathVariable("party-id") Long partyId,
                                       @Validated @RequestBody ChatStatusChangeRequestDto chatStatusChangeRequestDto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(ChatResponseMessage.ILLEGAL_CHAT_STATUS, bindingResult);
        }
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPDATE_CHAT);
    }

    @Operation(summary = "채팅방 최대 참여자 수 변경", description = "방장만 채팅방 속성을 변경할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 참여자 수를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}/number")
    public DefaultResponse changeGoalNumber(@PathVariable("party-id") Long partyId,
                                           @Schema(description = "채팅방 최대 참여자 수")
                                           @Validated @RequestBody ChatChangeGoalNumberRequestDto chatChangeGoalNumberRequestDto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(ChatResponseMessage.ILLEGAL_CHAT_GOAL_NUMBER, bindingResult);
        }
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPDATE_CHAT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // -> 글로벌로 이동하기
    public ErrorResponse illegalTypeConversion(MethodArgumentTypeMismatchException e) {
        return ErrorResponse.res(500, e.getMessage());
    }
}
