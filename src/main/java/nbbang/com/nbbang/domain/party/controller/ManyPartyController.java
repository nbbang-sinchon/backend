package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import nbbang.com.nbbang.domain.party.exception.IllegalPartyFindRequestException;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.exception.IllegalPlaceException;
import nbbang.com.nbbang.global.response.*;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @Operation(summary = "여러 개 파티 리스트 조회", description = "여러 개의 파티 리스트를 전송합니다. json이 아닌 query parameter로 데이터를 전송해야 합니다. 예시 : http://15.165.132.250:8094/parties?places=YEONHUI&places=CHANGCHEON&showOngoing=true&search=BHC")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyListResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 쿼리 파라미터를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @GetMapping("/parties")
    public ResponseEntity findParty(@ParameterObject @Validated @ModelAttribute PartyFindRequestDto partyFindRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, "BAD REQUEST"), BAD_REQUEST);
            throw new IllegalPartyFindRequestException();
        }
        /** 이 부분 검증 로직을 annotation 으로 어떻게 구현할지? */
        if (partyFindRequestDto.getPlacesString() != null) {
            List<String> places = partyFindRequestDto.getPlacesString();
            for (String p : places) {
                if (!(p.equals(Place.NONE.toString()) || p.equals(Place.SINCHON.toString()) || p.equals(Place.YEONHUI.toString()) || p.equals(Place.CHANGCHEON.toString()))) {
                    //return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ManyPartyResponseMessage.ILLEGAL_PLACE), BAD_REQUEST);
                    throw new IllegalPlaceException();
                }
            }
        }
        /**  ==================== 나중에 구현할 것 ==============  */

        Page<Party> queryResults = manyPartyService.findAllByRequestDto(partyFindRequestDto.createPageRequest(),
                PartyFindRequestFilterDto.createRequestFilterDto(partyFindRequestDto.getOngoing(), partyFindRequestDto.getSearch(), partyFindRequestDto.getPlaces()));
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_FIND_SUCCESS,
                PartyListResponseDto.createFromEntity(queryResults.getContent())), OK);
    }

    @ExceptionHandler(IllegalPartyFindRequestException.class)
    public ResponseEntity illegalPartyFindRequestExHandle(IllegalPartyFindRequestException e) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.BAD_REQUEST, ManyPartyResponseMessage.ILLEGAL_PARTY_LIST_REQUEST), BAD_REQUEST);
    }

}