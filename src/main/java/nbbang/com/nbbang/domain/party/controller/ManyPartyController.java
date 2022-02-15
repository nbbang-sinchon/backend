package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.PartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "parties", description = "여러 개 파티 조회")
@RestController
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
@Slf4j
@RequiredArgsConstructor
public class ManyPartyController {

    private final ManyPartyService manyPartyService;

    @Operation(summary = "여러 개 파티 리스트 조회", description = "여러 개의 파티 리스트를 전송합니다. json이 아닌 query parameter로 데이터를 전송해야 합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyListResponseDto.class)))
    @GetMapping("/parties")
    public ResponseEntity findParty(@ModelAttribute PartyFindRequestDto partyFindRequestDto) {
        Page<Party> queryResults = manyPartyService.findAllByRequestDto(partyFindRequestDto.createPageRequest(),
                PartyFindRequestFilterDto.createRequestFilterDto(partyFindRequestDto.getSearch()));
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessageParty.PARTY_FIND_SUCCESS,
                PartyListResponseDto.createFromEntity(queryResults.getContent())), OK);
    }
}