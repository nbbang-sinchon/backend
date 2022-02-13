package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindResponseDto;
import nbbang.com.nbbang.global.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "parties", description = "여러 개 파티 조회")
@RestController
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found")
})
@Slf4j
public class ManyPartyController {
    @Operation(summary = "여러 개 파티 리스트", description = "여러 개의 파티 리스트를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectMaker.class)))
    @GetMapping("/parties")
    public ResponseEntity findParty(@ModelAttribute PartyFindRequestDto partyFindRequestDto) {
        // @Validated, BindingResult bindingResult 넣어주기
        // log.info("errors={}", bindingResult);
        List<String> hashtags = Arrays.asList("BHC", "치킨");
        PartyFindResponseDto partyFindResponseDto = new PartyFindResponseDto("연희동 올리브영 앞 BHC 7시", LocalDateTime.now(), 3, 4, "마감 임박", hashtags);
        List<PartyFindResponseDto> collect = Arrays.asList(partyFindResponseDto);
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessageParty.PARTY_FIND_SUCCESS, new ObjectMaker(collect)), OK);
    }

}