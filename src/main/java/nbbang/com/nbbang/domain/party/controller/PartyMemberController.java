package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.exception.PartyExitForbiddenException;
import nbbang.com.nbbang.domain.party.exception.PartyJoinException;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyMemberController {
    private final PartyService partyService;
    private final MemberService memberService;
    private final PartyMemberService partyMemberService;

    @Operation(summary = "파티 참여", description = "파티에 참여합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "파티에 참여할 수 없습니다. 이미 참여한 파티이거나 파티가 찼습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/join")
    public DefaultResponse joinParty(@PathVariable("party-id") Long partyId) {
        Long memberId = 1L;
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);

        partyMemberService.joinParty(party, member);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_JOIN_SUCCESS);
    }

    @Operation(summary = "파티 탈퇴", description = "파티에서 탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "파티에서 탈퇴할 수 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Not Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/exit")
    public DefaultResponse exitParty(@PathVariable("party-id") Long partyId) {
        Long memberId = 1L;
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);

        partyMemberService.exitParty(party, member);
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_EXIT_SUCCESS);
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
