package nbbang.com.nbbang.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.dto.*;
import nbbang.com.nbbang.domain.party.dto.PartyListResponseDto;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.ResponseMessage;
import nbbang.com.nbbang.global.support.FileUpload.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member", description = "회원 관리 api (로그인 구현시 올바른 토큰을 보내지 않을 경우 401 Unauthorized 메시지를 받습니다.)")
@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json"))
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "마이페이지 정보 조회", description = "자신의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class)))
    @GetMapping("")
    public ResponseEntity select() {
        return new ResponseEntity(DefaultResponse.builder()
                .statusCode(200)
                .responseMessage(ResponseMessage.READ_USER)
                .data(MemberResponseDto.createMock())
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "마이페이지 정보 업데이트", description = "자신의 정보를 업데이트합니다.")
    @PatchMapping("")
    public ResponseEntity update(@RequestPart MemberUpdateRequestDto memberUpdateRequestDto) {
        return new ResponseEntity(DefaultResponse.builder()
                .statusCode(200)
                .responseMessage(ResponseMessage.UPDATE_USER)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다.")
    @DeleteMapping("")
    public ResponseEntity delete() {
        return new ResponseEntity(DefaultResponse.builder()
                .statusCode(204)
                .responseMessage(ResponseMessage.UPDATE_USER)
                .build(), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "프로필 사진 업로드", description = "프로필 사진을 업로드합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", implementation = MemberProfileImageUploadResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "프로필 사진 업로드 실패, 잘못된 요청입니다. 사진이 올바른지 확인하세요.", content = @Content(mediaType = "application/json"))
    @PostMapping(path = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadProfileImage(@Schema(description = "이미지 파일을 업로드합니다.")
                                                       @RequestPart MultipartFile imgFile) {
        String filePath = fileUploadService.fileUpload(imgFile);
        return new ResponseEntity(DefaultResponse.builder()
                .statusCode(200)
                .responseMessage(ResponseMessage.UPDATE_USER)
                .data(MemberProfileImageUploadResponseDto.createMock())
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "나의 파티", description = "자신이 속한 파티 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyListResponseDto.class)))
    @GetMapping("/parties")
    public ResponseEntity parties() {
        return new ResponseEntity(DefaultResponse.builder()
                .statusCode(200)
                .responseMessage(ResponseMessage.READ_USER)
                .data(PartyListResponseDto.createMock())
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "멤버 위치", description = "멤버의 위치 정보를 제공합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaceResponseDto.class)))
    @GetMapping("/place")
    public ResponseEntity memberPlace() {
        return new ResponseEntity(DefaultResponse.builder()
                .statusCode(200)
                .responseMessage(ResponseMessage.READ_USER)
                .data(new PlaceResponseDto("연희동"))
                .build(), HttpStatus.OK);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponseDtoWrapper<T> {
        private T member;
    }

}