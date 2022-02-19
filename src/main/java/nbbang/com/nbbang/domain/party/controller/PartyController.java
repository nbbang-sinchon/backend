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
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.*;
import nbbang.com.nbbang.domain.party.exception.PartyExitForbiddenException;
import nbbang.com.nbbang.domain.party.exception.PartyJoinException;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.exception.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.GlobalResponseMessage;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "party", description = "단일 파티 CRUD")
@ApiResponses(value = {
        //@ApiResponse(responseCode = "200", description = "OK",
        //        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
@RestController
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;
    private final ManyPartyService manyPartyService;
    private final MemberService memberService;

    @Operation(summary = "파티 생성", description = "파티를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class)))
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

        PartyFindRequestDto partyFindRequestDto = PartyFindRequestDto.builder().places(Arrays.asList("SINCHON"))
                .isOngoing(true).build();
        List<Party> parties = partyService.findNearAndSimilar(partyId);
        List<PartyFindResponseDto> collect = parties.stream().map(PartyFindResponseDto::createByEntity).collect(Collectors.toList());
        List<String> hashtags = party.getHashtagContents();
        PartyReadResponseDto partyReadResponseDto = PartyReadResponseDto.createDto(party, userId,  hashtags, collect);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_READ_SUCCESS, partyReadResponseDto);
    }

    @Operation(summary = "파티 수정", description = "파티를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}")
    public DefaultResponse updateParty(@PathVariable("party-id") Long partyId, @RequestBody PartyRequestDto partyRequestDtO) {
        partyService.updateParty(partyId, PartyUpdateServiceDto.createByPartyRequestDto(partyRequestDtO));
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS, new PartyIdResponseDto(partyId));
    }

/*    @Operation(summary = "파티 종료", description = "파티를 종료합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{party-id}")
    public DefaultResponse deleteParty(@PathVariable("party-id") Long partyId) {
        partyService.closeParty(partyId);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_DELETE_SUCCESS);
    }*/


    @Operation(summary = "파티 참여", description = "파티에 참여합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "파티에 참여할 수 없습니다. 이미 참여한 파티이거나 파티가 찼습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/join")
    public DefaultResponse joinParty(@PathVariable("party-id") Long partyId) {
        Long memberId = 1L;
        Party party = partyService.findParty(partyId);
        Member member = memberService.findById(memberId);

        partyService.joinParty(party, member);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_JOIN_SUCCESS);
    }

    @Operation(summary = "파티 탈퇴", description = "파티에서 탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "파티에서 탈퇴할 수 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/exit")
    public DefaultResponse exitParty(@PathVariable("party-id") Long partyId) {
        Long memberId = 1L;
        Party party = partyService.findParty(partyId);
        Member member = memberService.findById(memberId);

        partyService.exitParty(party, member);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_EXIT_SUCCESS);
    }

    @Operation(summary = "파티 상태 변경", description = "방장만 파티의 상태를 변경할 수 있습니다. OPEN, FULL, CLOSED")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. Status 를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}/status")
    public DefaultResponse changeStatus(@PathVariable("party-id") Long partyId,
                                        @Validated @RequestBody PartyStatusChangeRequestDto partyStatusChangeRequestDto,
                                        BindingResult bindingResult) {
        Long memberId = 1L; // 삭제 예정
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(PartyResponseMessage.ILLEGAL_PARTY_STATUS, bindingResult);
        }
        Party party = partyService.findParty(partyId);
        Member member = memberService.findById(memberId);
        partyService.changeStatus(party, member, partyStatusChangeRequestDto.createStatus());
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS);
    }

    @Operation(summary = "파티 최대 참여자 수 변경", description = "방장만 파티 속성을 변경할 수 있습니다. MAX 10, MIN 2")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 참여자 수를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}/number")
    public DefaultResponse changeGoalNumber(@PathVariable("party-id") Long partyId,
                                            @Schema(description = "파티 최대 참여자 수")
                                            @Validated @RequestBody PartyChangeGoalNumberRequestDto partyChangeGoalNumberRequestDto,
                                            BindingResult bindingResult) {
        Long memberId = 1L;
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(PartyResponseMessage.ILLEGAL_PARTY_GOAL_NUMBER, bindingResult);
        }
        Party party = partyService.findParty(partyId);
        Member member = memberService.findById(memberId);
        partyService.changeGoalNumber(party, member, partyChangeGoalNumberRequestDto.getGoalNumber());
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS);
    }

    @ExceptionHandler(PartyJoinException.class)
    public ErrorResponse partyJoinExHandle(PartyJoinException e) {
        return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(PartyExitForbiddenException.class)
    public ErrorResponse partyExitExHandle(PartyExitForbiddenException e) {
        return new ErrorResponse(StatusCode.FORBIDDEN, e.getMessage());
    }

}