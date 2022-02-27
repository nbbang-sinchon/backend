package nbbang.com.nbbang.domain.chat.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Modifier;

import static java.lang.reflect.Modifier.PRIVATE;

@Tag(name = "ChatSocketDevelop", description = "채팅방의 소켓 관련된 API입니다.")
@NoArgsConstructor(access = AccessLevel.PRIVATE) // ONLY FOR SWAGGER. Don't do anything for Logic.
@RestController
public class ChatSocketAPI {

    @Operation(summary = "[Socket] 채팅 메시지 전송", description = "다른 사람이 채팅 메시지를 전송하면, 채팅방 내의 유저들이 받는 소켓입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatSendResponseDto.class)))
    @PostMapping("/chat/develop")
    public void socketEmitApi(Long memberId, Long partyId){
    }

    @Operation(summary = "[Socket] 채팅 읽음", description = "다른 사람이 채팅방에 들어와 메시지를 읽으면, 채팅방 내의 유저들이 받는 소켓입니다. lastReadMessageId보다 큰 id를 갖는 메시지들은 전부 읽지 않은 사람 수를 -1 해주세요.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatReadSocketDto.class)))
    @PostMapping("/chat/develop/read")
    public void readMessage(Long memberId, Long partyId){
    }
}
