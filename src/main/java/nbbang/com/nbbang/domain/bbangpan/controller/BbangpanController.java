package nbbang.com.nbbang.domain.bbangpan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.dto.BbangpanGetResponseDto;
import nbbang.com.nbbang.domain.bbangpan.dto.MemberBbangpanDto;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@Tag(name = "bbangpan", description = "빵판과 관련된 API입니다.")
@RestController
@RequestMapping("/bbangpans")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found")
})
public class BbangpanController {

    @Operation(summary = "빵판 정보", description = "유저가 빵판을 클릭했을 때, 필요한 정보를 보냅니다.")
    @GetMapping("{/party_id}")
    public BbangpanGetResponseDto getBbanpan(@PathVariable Long party_id) {
        List<MemberBbangpanDto> memberBbangpanDtos = new ArrayList<>();
        memberBbangpanDtos.add(new MemberBbangpanDto("연희동 주민", 2000, "송금 완료"));
        return new BbangpanGetResponseDto(memberBbangpanDtos, 2000, 4000, 6000);
    }

}
