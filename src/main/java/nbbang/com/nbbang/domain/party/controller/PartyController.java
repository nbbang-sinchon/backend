package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyIdResponseDto;
import nbbang.com.nbbang.domain.party.dto.PartyReadResponseDto;
import nbbang.com.nbbang.domain.party.dto.PartyRequestDto;
import nbbang.com.nbbang.domain.party.service.HashtagService;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.GlobalResponseMessage;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@Tag(name = "party", description = "단일 파티 CRUD")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
@RestController
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;
    private final HashtagService hashtagService;

    @Operation(summary = "파티 생성", description = "파티를 생성합니다.")
    @PostMapping
    public DefaultResponse createParty(@Valid @RequestBody PartyRequestDto partyRequestDtO, BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
       Long partyId = partyService.createParty(partyRequestDtO.createByDto(),partyRequestDtO.getHashtags());
       return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_CREATE_SUCCESS, new PartyIdResponseDto(partyId));
    }

    @Operation(summary = "파티 상세", description = "파티의 상세 정보입니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyReadResponseDto.class)))
    @GetMapping("/{party-id}")
    public DefaultResponse readParty(@PathVariable("party-id") Long partyId){
        Long userId = 1L; //세션 구현 후 수정
        Party party = partyService.findParty(partyId);
        List<Party> parties = null; // ManyPartyService에서 가져오기
        List<String> hashtags = partyService.findHashtagContentsByParty(party);
        PartyReadResponseDto partyReadResponseDto = PartyReadResponseDto.createDto(party, userId,  hashtags, parties);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_READ_SUCCESS, partyReadResponseDto);
    }


    @Operation(summary = "파티 수정", description = "파티를 수정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}")
    public DefaultResponse updateParty(@PathVariable("party-id") Long partyId, @RequestBody PartyRequestDto partyRequestDtO) {
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS);
    }


    @Operation(summary = "파티 삭제", description = "파티를 삭제합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{party-id}")
    public DefaultResponse deleteParty(@PathVariable("party-id") Long partyId) {
        partyService.deleteParty(partyId);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_DELETE_SUCCESS);

    }

}
