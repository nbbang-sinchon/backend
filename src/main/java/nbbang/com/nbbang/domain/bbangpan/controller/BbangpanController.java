package nbbang.com.nbbang.domain.bbangpan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanGetResponseDto;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanPriceChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanStatusChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.MemberBbangpanDto;
import nbbang.com.nbbang.global.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@Tag(name = "bbangpan", description = "빵판과 관련된 API입니다.")
@RestController
@RequestMapping("/bbangpans/{party-id}")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
public class BbangpanController {
    @Operation(summary = "빵판 정보", description = "유저가 빵판을 클릭했을 때, 필요한 정보를 보냅니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BbangpanGetResponseDto.class)))
    @GetMapping
    public ResponseEntity readBbangpan(@PathVariable("party-id") Long partyId) {
        // 파티 멤버가 되는 로직을 추가한다.
        List<MemberBbangpanDto> memberBbangpanDtos = new ArrayList<>();
        memberBbangpanDtos.add(new MemberBbangpanDto("연희동 주민", 2000, "송금 완료"));
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.BBANGPAN_READ_SUCCESS,
                new BbangpanGetResponseDto(memberBbangpanDtos, 2000, 4000, 6000)), OK);
    }

    @Operation(summary = "주문 금액 설정", description = "유저가 주문 금액을 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/price")
    public ResponseEntity changePrice(@PathVariable("party-id") Long partyId, @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto) {
        // 프론트에 socket.emit 보내면 주문 금액도 바뀌어 보이고 총 금액도 바뀌어 보여야함
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.PRICE_CHANGE_SUCCESS), OK);

    }

    @Operation(summary = "배달비 설정", description = "방장이 배달비를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PostMapping("/delivery-fee")
    public ResponseEntity changeDeliveryFee(@PathVariable("party-id") Long partyId, @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.DELIVERYFEE_CHANGE_SUCCESS), OK);
    }

    @Operation(summary = "송금 상태 설정", description = "송금 상태를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/send-status")
    public ResponseEntity changeSendStatus(@PathVariable("party-id") Long partyId, @RequestBody BbangpanStatusChangeRequestDto bbangpanStatusChangeRequestDto) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.SENDSTATUS_CHANGE_SUCCESS), OK);

    }

}
