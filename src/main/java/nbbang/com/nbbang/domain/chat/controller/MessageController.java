package nbbang.com.nbbang.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendRequestDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.global.error.GlobalErrorResponseMessage;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.socket.SocketSender;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "Chat", description = "채팅 메시지를 전송합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats/{party-id}")
public class MessageController {

    private final MessageService messageService;
    private final CurrentMember currentMember;
    private final SocketSender socketSender;


    @Operation(summary = "채팅 메시지 전송", description = "채팅 메시지를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatSendResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping
    public DefaultResponse sendMessage(@PathVariable("party-id") Long partyId, @Valid @RequestBody ChatSendRequestDto chatSendRequestDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        messageService.send(partyId, currentMember.id(), chatSendRequestDto.getContent());
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPLOADED_MESSAGE);
    }

    @Operation(summary = "채팅 사진 전송", description = "채팅 사진을 전송합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatSendResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @PostMapping(path = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefaultResponse sendImage(@PathVariable("party-id") Long partyId, @RequestPart MultipartFile imgFile) throws Exception {
        messageService.sendImage(partyId, currentMember.id(), imgFile);
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPLOADED_IMAGE_MESSAGE);
    }
}
