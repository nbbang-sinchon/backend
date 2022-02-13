package nbbang.com.nbbang.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.*;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.ResponseMessage;
import nbbang.com.nbbang.global.response.StatusCode;
import nbbang.com.nbbang.global.support.FileUpload.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Chat", description = "채팅방 api (로그인 구현시 올바른 토큰을 보내지 않을 경우 401 Unauthorized 메시지를 받습니다.)")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final FileUploadService fileUploadService;

    @Operation(summary = "채팅방 조회", description = "채팅방을 파티 id 로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "채팅방의 멤버가 아닙니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/chats/{party_id}")
    public ResponseEntity select(@PathVariable("party_id") Long partyId) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_CHAT, ChatResponseDto.createMock()), HttpStatus.OK);
    }

    @Operation(summary = "채팅 메시지 전송", description = "채팅 메시지를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "채팅방의 멤버가 아닙니다..", content = @Content(mediaType = "application/json"))
    @PostMapping("/chats/{party_id}/messages")
    public ResponseEntity sendMessage(@PathVariable("party_id") Long partyId, @RequestBody ChatMessageSendRequestDto chatMessageSendRequestDto) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPLOADED_MESSAGE), HttpStatus.OK);
    }

    @Operation(summary = "메시지 사진 업로드", description = "메시지 사진을 업로드합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatMessageImageUploadResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @PostMapping(path = "/chats/{party_id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity sendImage(@PathVariable("party_id") Long partyId,
                                                       @Schema(description = "이미지 파일을 업로드합니다.")
                                                       @RequestPart MultipartFile imgFile) {
        String filePath = fileUploadService.fileUpload(imgFile);
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPLOADED_MESSAGE, new ChatMessageImageUploadResponseDto(filePath)), HttpStatus.OK);
    }

    @Operation(summary = "채팅방에서 나가기", description = "채팅방에서 나갑니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "방장 또는 참여 회원이 아닙니다.", content = @Content(mediaType = "application/json"))
    @PostMapping("/chats/{party_id}/out")
    public ResponseEntity exitChat(@PathVariable("party_id") Long partyId) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.EXIT_CHAT), HttpStatus.OK);
    }

    @Operation(summary = "채팅방 상태 변경", description = "방장만 채팅방 속성을 변경할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. Status 를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "방장이 아닙니다.", content = @Content(mediaType = "application/json"))
    @PatchMapping("/chats/{party_id}/status")
    public ResponseEntity changeStatus(@PathVariable("party_id") Long partyId,
                                       @Schema(description = "채팅방 상태: 모집 중 - running, 마감 임박 - imminent, 모집 완료 - full, 주문 완료 - complete, 모집 취소 - cancel")
                                               ChatStatusChangeRequestDto chatStatusChangeRequestDto) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_CHAT), HttpStatus.OK);
    }

    @Operation(summary = "채팅방 최대 참여자 수 변경", description = "방장만 채팅방 속성을 변경할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "채팅방 최대 참여자 수 변경 성공", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. Status 를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "방장이 아닙니다.", content = @Content(mediaType = "application/json"))
    @PatchMapping("/chats/{party_id}/number")
    public ResponseEntity changeMaxMemberNumber(@PathVariable("party_id") Long partyId,
                                                @Schema(description = "채팅방 최대 참여자 수")
                                                        ChatChangeGoalNumberRequestDto chatChangeGoalNumberRequestDto) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_CHAT), HttpStatus.OK);
    }

}
