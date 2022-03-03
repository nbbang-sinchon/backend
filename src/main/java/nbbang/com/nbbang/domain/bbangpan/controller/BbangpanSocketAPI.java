package nbbang.com.nbbang.domain.bbangpan.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.dto.socket.BbangpanDeliveryFeeChangeSocketDto;
import nbbang.com.nbbang.domain.bbangpan.dto.socket.PartyMemberFieldChangeSocketDto;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BbangSocketDevelop", description = "빵판의 소켓 관련된 API입니다.")
@NoArgsConstructor(access = AccessLevel.PRIVATE) // ONLY FOR SWAGGER. Don't do anything for Logic.
@RestController
@RequestMapping("bbangpan/develop")
public class BbangpanSocketAPI {

    @Operation(summary = "주문 금액, 송금 상태 설정", description = "유저가 주문 금액, 송금 상태를 설정합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyMemberFieldChangeSocketDto.class)))
    @PostMapping("/price")
    public void changePrice() {
    }

    @Operation(summary = "배달비 설정", description = "방장이 배달비를 설정합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BbangpanDeliveryFeeChangeSocketDto.class)))
    @PostMapping("/delivery-fee")
    public void changeDeliveryFee() {
    }
}
