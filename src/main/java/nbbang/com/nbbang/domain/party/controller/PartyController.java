package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "party", description = "테스트 api")
@RestController
public class PartyController {

    @Operation(summary = "파티 생성", description = "파티를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/parties")
    public void createParty(
            @ModelAttribute CreatePartyRequestDto createPartyRequestDtO) {

    }
/*    제목, 본문, 해시태그를 작성하고 음식 유형을 선택한다.
    제목과 모집 인원수는 필수, 본문과 해시태그는 선택이다.
    해시태그는 10개 이하로 제한한다.
    위치(신촌동/연희동/창천동)를 선택할 수 있다.
    모집 인원수를 선택할 수 있다.
    */

    @Operation(summary = "파티 관리", description = "파티를 관리합니다.")
    @PatchMapping("/parties/{id}")
    public void updateParty(@PathVariable Long id, @ModelAttribute CreatePartyRequestDto createPartyRequestDtO) {

    }

    @Operation(summary = "파티 상세", description = "파티를 상세 조회합니다.")
    @GetMapping("/parties/{party_id}")
    public ReadPartyResponseDto readParty(@PathVariable Long id){
        List<String> hashtags = new ArrayList<>();
        hashtags.add("BHC");
        hashtags.add("치킨");
        return new ReadPartyResponseDto("BHC 7시", "연희동 올리브영 앞에서 나눠요", "7분 전",
                "연히동", 3, 4, "마감 임박",
                "연희동 주민", 10, hashtags);
    }

    @Operation(summary = "파티 삭제", description = "파티를 삭제합니다.")
    @DeleteMapping("/parties/{id}")
    public void deleteParty(@PathVariable Long id,
            @ModelAttribute CreatePartyRequestDto createPartyRequestDtO) {

    }
// 모집 상태 변경

    @Data @NoArgsConstructor @AllArgsConstructor
    static class CreatePartyRequestDto {
        private String title;
        private String content;
        private List<HashtagDto> hashtags;
        private String place;
        private Integer goalNumber;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class HashtagDto {
        private String content;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class ReadPartyResponseDto {
        private String title;
        private String content;
        private String passTime; // 경과 시간을 계산해서 보내주기
        private String place;
        private Integer joinNumber;
        private Integer goalNumber;
        private String partyStatus;
        private String nickname;
        private Integer breadNumber;
        private List<String> hashtags;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class SampleDto {
        private String title;
        private String content;
    }

    @Schema(description = "샘플 생성 폼")
    @Data
    static class PartyForm {

        @Schema(description = "샘플 제목", nullable = false, example = "BHC 뿌링클 오늘 7시", maxLength = 20)
        private String title;

        @Schema(description = "샘플 내용")
        private String content;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity notExistExceptionHandler(IllegalStateException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
