package nbbang.com.nbbang.domain.bbangpan.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanPriceChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanPriceChangeSocketDto;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.global.error.GlobalErrorResponseMessage;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "BbangSocketDevelop", description = "빵판의 소켓 관련된 API입니다.")
@NoArgsConstructor(access = AccessLevel.PRIVATE) // ONLY FOR SWAGGER. Don't do anything for Logic.
@RestController
@RequestMapping("bbangpan/develop")
public class BbangpanSocketAPI {

    @Operation(summary = "주문 금액 설정", description = "유저가 주문 금액을 설정합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BbangpanPriceChangeSocketDto.class)))
    @PostMapping("/price")
    public void changePrice() {
    }


    @Operation(summary = "[Socket] 채팅 읽음", description = "다른 사람이 채팅방에 들어와 메시지를 읽으면, 채팅방 내의 유저들이 받는 소켓입니다. lastReadMessageId보다 큰 id를 갖는 메시지들은 전부 읽지 않은 사람 수를 -1 해주세요.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatReadSocketDto.class)))
    @PostMapping("/chat/develop/read")
    public void readMessage(Long memberId, Long partyId){
    }
}
