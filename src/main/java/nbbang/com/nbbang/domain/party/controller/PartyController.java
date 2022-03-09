package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import nbbang.com.nbbang.domain.party.dto.single.*;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyChangeGoalNumberRequestDto;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyRequestDto;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyStatusChangeRequestDto;
import nbbang.com.nbbang.domain.party.dto.single.response.PartyIdResponseDto;
import nbbang.com.nbbang.domain.party.dto.single.response.PartyReadResponseDto;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.domain.party.validation.PartyCreateGroup;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.error.GlobalErrorResponseMessage;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Party", description = "단일 파티 생성, 수정, 조회를 지원합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;
    private final MemberService memberService;
    private final CurrentMember currentMember;

    @Operation(summary = "파티 생성", description = "파티를 생성합니다. Place 는 none, sinchon, yeonhui, changcheon 중 하나, 모집 인원은 2~10, 해시태그는 중복 없이 10개 이하")
    @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class)))
    @PostMapping
    public DefaultResponse createParty(@Parameter @Validated({PartyCreateGroup.class, Default.class}) @RequestBody PartyRequestDto partyRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        Member member = memberService.findById(currentMember.id());
        Party party = partyRequestDto.createEntityByDto();
        Party createParty = partyService.create(party, currentMember.id(), partyRequestDto.getHashtags());
        Long partyId = partyService.findIdByParty(createParty);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_CREATE_SUCCESS, PartyIdResponseDto.builder().id(partyId).build());
    }

    @Operation(summary = "파티 상세", description = "파티의 상세 정보입니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyReadResponseDto.class)))
    @GetMapping("/{party-id}")
    public DefaultResponse readParty(@PathVariable("party-id") Long partyId){
        Party party = partyService.findById(partyId);
        List<Party> parties = partyService.findNearAndSimilar(partyId);
        List<PartyFindResponseDto> collect = parties.stream().map(PartyFindResponseDto::createByEntity).collect(Collectors.toList());
        List<String> hashtags = party.getHashtagContents();
        PartyReadResponseDto partyReadResponseDto = PartyReadResponseDto.createDto(party, currentMember.id(), hashtags, collect);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_READ_SUCCESS, partyReadResponseDto);
    }

    @Operation(summary = "파티 수정", description = "파티를 수정합니다. Place 는 none, sinchon, yeonhui, changcheon 중 하나, 모집 인원은 2~10, 해시태그는 중복 없이 10개 이하")
    @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyIdResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}")
    public DefaultResponse updateParty(@PathVariable("party-id") Long partyId, @Valid @RequestBody PartyRequestDto partyRequestDtO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        partyService.update(partyId, PartyUpdateServiceDto.createByPartyRequestDto(partyRequestDtO), currentMember.id());
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS, new PartyIdResponseDto(partyId));
    }

    @Operation(summary = "파티 상태 변경", description = "방장만 파티의 상태를 변경할 수 있습니다. OPEN, FULL, CLOSED")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. Status 를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}/status")
    public DefaultResponse changeStatus(@PathVariable("party-id") Long partyId,
                                        @Validated @RequestBody PartyStatusChangeRequestDto partyStatusChangeRequestDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(PartyResponseMessage.ILLEGAL_PARTY_STATUS, bindingResult);
        }
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(currentMember.id());
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
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(PartyResponseMessage.ILLEGAL_PARTY_GOAL_NUMBER, bindingResult);
        }
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(currentMember.id());
        partyService.changeGoalNumber(party, member, partyChangeGoalNumberRequestDto.getGoalNumber());
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS);
    }


}