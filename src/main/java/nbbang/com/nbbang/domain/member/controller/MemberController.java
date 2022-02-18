package nbbang.com.nbbang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.*;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import nbbang.com.nbbang.global.support.FileUpload.FileUploadService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "Member", description = "회원 관리 api (로그인 구현시 올바른 토큰을 보내지 않을 경우 401 Unauthorized 메시지를 받습니다.)")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final FileUploadService fileUploadService;
    private final MemberService memberService;
    private final ManyPartyService manyPartyService;
    private Long memberId = 1L; // 로그인 기능 구현 후 삭제 예정

    @Operation(summary = "테스트 용도 멤버 생성")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @PostMapping("/create")
    public DefaultResponse create() {
        memberId = memberService.saveMember("루피", Place.SINCHON);
        return DefaultResponse.res(StatusCode.OK, "테스트 멤버가 생성되었습니다.");
    }
    
    @Operation(summary = "마이페이지 정보 조회", description = "자신의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class)))
    @GetMapping
    public DefaultResponse select() {
        Member member = memberService.findById(memberId);
        MemberResponseDto dto = MemberResponseDto.createByEntity(member);
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.READ_MEMBER, dto);
    }

    @Operation(summary = "마이페이지 정보 업데이트", description = "자신의 정보를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "회원 정보를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @PatchMapping
    public DefaultResponse update(@Validated @RequestBody MemberUpdateRequestDto memberUpdateRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(MemberResponseMessage.ILLEGAL_MEMBER_UPDATE_REQUEST, bindingResult);
        }
        memberService.updateMember(memberId, memberUpdateRequestDto.getNickname(), memberUpdateRequestDto.getPlace());
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.UPDATE_MEMBER);
    }


    @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @DeleteMapping
    public DefaultResponse delete() {
        memberService.deleteMember(memberId);
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.DELETE_MEMBER);
    }

    @Operation(summary = "프로필 사진 업로드(미구현)", description = "프로필 사진을 업로드합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", implementation = MemberProfileImageUploadResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "프로필 사진 업로드 실패, 잘못된 요청입니다. 사진이 올바른지 확인하세요.", content = @Content(mediaType = "application/json"))
    @PostMapping(path = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefaultResponse uploadProfileImage(@Schema(description = "이미지 파일을 업로드합니다.")
                                             @RequestPart MultipartFile imgFile) {
        String filePath = fileUploadService.fileUpload(imgFile);
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.UPDATE_MEMBER, MemberProfileImageUploadResponseDto.createMock());
    }

    @Operation(summary = "나의 파티", description = "자신이 속한 파티 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyListResponseDto.class)))
    @GetMapping("/parties")
    public DefaultResponse parties(@ParameterObject PageableDto pageableDto) {
        List<Party> myParties = manyPartyService.findMyParties(pageableDto.createPageRequest(), memberId).getContent();
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.READ_MEMBER, PartyListResponseDto.createFromEntity(myParties));
    }

    @Operation(summary = "멤버 위치", description = "멤버의 위치 정보를 제공합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceResponseDto.class)))
    @GetMapping("/place")
    public DefaultResponse memberPlace() {
        Member member = memberService.findById(memberId);
        return DefaultResponse.res(StatusCode.OK, MemberResponseMessage.MEMBER_LOCATION_SUCCESS, PlaceResponseDto.create(member.getPlace()));
    }

}