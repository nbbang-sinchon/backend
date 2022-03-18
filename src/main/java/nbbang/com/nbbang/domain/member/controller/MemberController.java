package nbbang.com.nbbang.domain.member.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.*;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;


@Tag(name = "Member", description = "회원 관리 api")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CurrentMember currentMember;

    @Operation(summary = "마이페이지 정보 조회", description = "자신의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class)))
    @GetMapping
    public DefaultResponse select(HttpServletRequest request) {
        Member member = memberService.findById(currentMember.id());
        Boolean isThereNotReadMessage = memberService.isThereNotReadChat(currentMember.id());
        MemberResponseDto dto = MemberResponseDto.createByEntity(member, isThereNotReadMessage);
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.READ_MEMBER, dto);
    }

    @Operation(summary = "마이페이지 정보 업데이트", description = "자신의 정보를 업데이트합니다. 닉네임은 1~16자, ")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "회원 정보를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @PatchMapping
    public DefaultResponse update(@Validated @RequestBody MemberUpdateRequestDto memberUpdateRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(MemberResponseMessage.ILLEGAL_MEMBER_UPDATE_REQUEST, bindingResult);
        }
        memberService.updateMember(currentMember.id(), memberUpdateRequestDto.getNickname(), memberUpdateRequestDto.getPlace());
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.UPDATE_MEMBER);
    }


    @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @DeleteMapping
    public DefaultResponse delete() {
        memberService.deleteMember(currentMember.id());
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.DELETE_MEMBER);
    }

    @Operation(summary = "프로필 사진 업로드", description = "프로필 사진을 업로드합니다. 기존 사진이 있으면 대체합니다. 10MB 이하, jpg, jpeg, jfif, png 포맷을 지원합니다. content-type : multipart/form-data; boundary=----WebKitFormBoundaryMHbwDI3md9KAPlGo " +
            "큰 파일은 서버에 오지 않게 해주세요. request body 에 imgFile 로 multipart 파일을 보내야 합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", implementation = MemberProfileImageUploadResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "프로필 사진 업로드 실패, 잘못된 요청입니다. 사진이 올바른지 확인하세요.", content = @Content(mediaType = "application/json"))
    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefaultResponse uploadAndUpdateAvatar(@Schema(description = "이미지 파일을 업로드합니다.")
                                             @RequestParam MultipartFile imgFile) throws IOException {
        String avatarUrl = memberService.uploadAndUpdateAvatar(currentMember.id(), imgFile);
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.UPDATE_MEMBER, MemberProfileImageUploadResponseDto.createByString(avatarUrl));
    }


    @Operation(summary = "멤버 위치", description = "멤버의 위치 정보를 제공합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceResponseDto.class)))
    @GetMapping("/place")
    public DefaultResponse memberPlace() {
        Member member = memberService.findById(currentMember.id());
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.MEMBER_LOCATION_SUCCESS, PlaceResponseDto.create(member.getPlace()));
    }

    @Hidden
    @GetMapping("/loginFail")
    public ErrorResponse memberLoginFail() {
        return ErrorResponse.res(StatusCode.UNAUTHORIZED, "로그인 실패");
    }


}