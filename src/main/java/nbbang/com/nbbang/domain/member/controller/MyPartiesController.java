package nbbang.com.nbbang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.controller.ManyPartyResponseMessage;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.my.MyClosedPartyListRequestDto;
import nbbang.com.nbbang.domain.party.dto.my.MyOnPartyListRequestDto;
import nbbang.com.nbbang.domain.party.dto.my.MyPartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.domain.partymember.service.PartyMemberService;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MyParties", description = "나의 파티 api")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/members/parties")
@RequiredArgsConstructor
public class MyPartiesController {

    private final ManyPartyService manyPartyService;
    private final PartyMemberService partyMemberService;
    private final CurrentMember currentMember;

    @Operation(summary = "나의 참여중인 파티", description = "자신이 속한 참여 중(OPEN, FULL)인 파티 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyPartyListResponseDto.class)))
    @GetMapping("/on")
    public DefaultResponse partiesOn(@ParameterObject MyOnPartyListRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(ManyPartyResponseMessage.ILLEGAL_PARTY_LIST_REQUEST, bindingResult);
        }
        Page<Party> res = manyPartyService.findAllParties(requestDto.createPageRequest(), requestDto.createPartyListRequestFilterDto(), requestDto.getCursorId(), currentMember.id());
        List<Integer> notReadNumbers = partyMemberService.getNotReadNumbers(res.getContent(), currentMember.id());
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.READ_MY_PARTY, MyPartyListResponseDto.createFromEntity(res.getContent(), currentMember.id(), notReadNumbers));
    }

    @Operation(summary = "나의 종료된 파티", description = "자신이 속한 종료된 파티 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyPartyListResponseDto.class)))
    @GetMapping("/closed")
    public DefaultResponse partiesClosed(@ParameterObject MyClosedPartyListRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(ManyPartyResponseMessage.ILLEGAL_PARTY_LIST_REQUEST, bindingResult);
        }
        Page<Party> res = manyPartyService.findAllParties(requestDto.createPageRequest(), requestDto.createPartyListRequestFilterDto(), requestDto.getCursorId(), currentMember.id());
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.READ_MY_PARTY, MyPartyListResponseDto.createFromEntity(res.getContent(), currentMember.id()));
    }

}