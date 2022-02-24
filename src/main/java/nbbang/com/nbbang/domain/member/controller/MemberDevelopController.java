package nbbang.com.nbbang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.dto.MemberListResponseDto;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import nbbang.com.nbbang.global.FileUpload.FileUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Tag(name = "MemberDevelop", description = "회원 관리 테스트용 api 로그인을 하지 않은 경우 ID=1 인 회원(루피)으로 표시됩니다.")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/members/develop")
@RequiredArgsConstructor
public class MemberDevelopController {

    private final FileUploadService fileUploadService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ManyPartyService manyPartyService;

    /*@Operation(summary = "테스트 용도 멤버 생성")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @PostMapping("/create")
    public DefaultResponse create(String nickname, String place) {
        if (nickname == null) {
            nickname = "루피";
        }
        if (place == null) {
            place = "SINCHON";
        }
        memberService.saveMember(nickname, Place.valueOf(place.toUpperCase(Locale.ROOT)));
        return DefaultResponse.res(StatusCode.OK, "테스트 멤버가 생성되었습니다.");
    }

    @Operation(summary = "테스트 용도 멤버 생성 후 로그인")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @PostMapping("/create/set")
    public DefaultResponse createAndSetMe(String nickname, String place) {
        if (nickname == null) {
            nickname = "루피";
        }
        if (place == null) {
            place = "SINCHON";
        }
        Long memberId = memberService.saveMember(nickname, Place.valueOf(place.toUpperCase(Locale.ROOT)));

        return DefaultResponse.res(StatusCode.OK, "테스트 멤버가 생성되었고 로그인 되었습니다.");
    }*/

    @Operation(summary = "모든 멤버 조회")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @PostMapping("/select/all")
    public DefaultResponse selectAll() {
        return DefaultResponse.res(StatusCode.OK, "모든 멤버를 조회합니다.", MemberListResponseDto.createByEntity(memberRepository.findAll()));
    }
}