package nbbang.com.nbbang.domain.bbangpan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanGetResponseDto;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanPriceChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanStatusChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.MemberBbangpanDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "bbangpan", description = "빵판과 관련된 API입니다.")
@RestController
@RequestMapping("/bbangpans/{party_id}")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found")
})
@Slf4j
public class BbangpanController {

    @Operation(summary = "빵판 정보", description = "유저가 빵판을 클릭했을 때, 필요한 정보를 보냅니다.")
    @GetMapping
    public BbangpanGetResponseDto getInfo(@PathVariable Long party_id) {
        // 파티 멤버가 되는 로직을 추가한다.
        List<MemberBbangpanDto> memberBbangpanDtos = new ArrayList<>();
        memberBbangpanDtos.add(new MemberBbangpanDto("연희동 주민", 2000, "송금 완료"));
        return new BbangpanGetResponseDto(memberBbangpanDtos, 2000, 4000, 6000);
    }

    @ApiResponse(responseCode = "403", description = "Not Party Member")
    @Operation(summary = "주문 금액 설정", description = "유저가 주문 금액을 설정합니다.")
            // content = @Content(schema = @Schema(implementation = BbangpanGetResponseDto.class)))
    @PostMapping("/price")
    public void changePrice(@PathVariable Long party_id, @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto) {
        // 프론트에 socket.emit 보내면 주문 금액도 바뀌어 보이고 총 금액도 바뀌어 보여야함
    }

    @ApiResponse(responseCode = "403", description = "Not Owner")
    @Operation(summary = "배달비 설정", description = "방장이 배달비를 설정합니다.")
    @PostMapping("/delivery-fee")
    public void changeDeliveryFee(@PathVariable Long party_id, @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto) {

    }

    @ApiResponse(responseCode = "403", description = "Not Party Member")
    @Operation(summary = "송금 상태 설정", description = "송금 상태를 설정합니다.")
    @PostMapping("/send-status")
    public void changeSendStatus(@PathVariable Long party_id, @RequestBody BbangpanStatusChangeRequestDto bbangpanStatusChangeRequestDto) {

    }

}
