package nbbang.com.nbbang.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.ChatMessageImageUploadResponseDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendRequestDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import nbbang.com.nbbang.global.support.FileUpload.FileUploadService;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Chat", description = "채팅 메시지 전송")
@ApiResponses(value = {
        //@ApiResponse(responseCode = "200", description = "OK",
        //        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Controller
@Slf4j
@RestController
@RequestMapping("/chats/{party-id}")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final FileUploadService fileUploadService;

    @Operation(summary = "채팅 메시지 전송", description = "채팅 메시지를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping
    public DefaultResponse<Object> sendMessage(@PathVariable("party-id") Long partyId, @RequestBody ChatSendRequestDto chatSendRequestDto, BindingResult bindingResult) {
        Long memberId = 1L;
        Long messageId = messageService.send(partyId, memberId, chatSendRequestDto.getContent());
        return DefaultResponse.res(StatusCode.OK, ChatResponseMessage.UPLOADED_MESSAGE);
    }

    @SendTo("/topic/{party-id}")
    public ChatSendResponseDto publishSendMessage(Long messageId){
        Message message = messageService.findById(messageId);
        ChatSendResponseDto.createByMessage(message);

        return ChatSendResponseDto.builder().context("hello").build();
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
}
