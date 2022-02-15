package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.PartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.response.*;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@Tag(name = "parties", description = "여러 개 파티 조회")
@RestController
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
@Slf4j
@RequiredArgsConstructor
public class ManyPartyController {

    private final ManyPartyService manyPartyService;

    @Operation(summary = "여러 개 파티 리스트 조회", description = "여러 개의 파티 리스트를 전송합니다. json이 아닌 query parameter로 데이터를 전송해야 합니다. `\n` 예시 : http://15.165.132.250:8094/parties?showOngoing=true&search=뿌링클")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyListResponseDto.class)))
    @GetMapping("/parties")
    public ResponseEntity findParty(@ParameterObject @ModelAttribute PartyFindRequestDto partyFindRequestDto) {
        /** 이 부분 검증 로직을 annotation 으로 어떻게 구현할지? */
        List<String> places = partyFindRequestDto.getPlacesString();
        for (String p : places) {
            if (!(p.equals(Place.NONE.toString()) || p.equals(Place.SINCHON) || p.equals(Place.YEONHUI) || p.equals(Place.CHANGCHEON))) {
                return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, "위치 정보를 올바르게 입력하세요."), BAD_REQUEST);

            }
        }

        Page<Party> queryResults = manyPartyService.findAllByRequestDto(partyFindRequestDto.createPageRequest(),
                PartyFindRequestFilterDto.createRequestFilterDto(partyFindRequestDto.getShowOngoing(), partyFindRequestDto.getSearch(), partyFindRequestDto.getPlaces()));
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessageParty.PARTY_FIND_SUCCESS,
                PartyListResponseDto.createFromEntity(queryResults.getContent())), OK);
    }
}