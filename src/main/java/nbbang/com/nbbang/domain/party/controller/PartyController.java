package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyReadResponseDto;
import nbbang.com.nbbang.domain.party.dto.PartyRequestDto;
import nbbang.com.nbbang.domain.party.service.HashtagService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@Tag(name = "party", description = "단일 파티 CRUD")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
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
    public ResponseEntity createParty(@Validated @RequestBody PartyRequestDto partyRequestDtO, BindingResult bindingResult) {
       if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }

        log.info("{}", "hello");
       // Long partyId = partyService.createParty(Party.builder().title("hello").build(),partyRequestDtO.getHashtags() );
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_CREATE_SUCCESS), OK);
    }

/*    제목, 본문, 해시태그를 작성하고 음식 유형을 선택한다.
    제목과 모집 인원수는 필수, 본문과 해시태그는 선택이다.
    해시태그는 10개 이하로 제한한다.
    위치(신촌동/연희동/창천동)를 선택할 수 있다.
    모집 인원수를 선택할 수 있다.
    */

    @Operation(summary = "파티 수정", description = "파티를 수정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{party-id}")
    public ResponseEntity updateParty(@PathVariable("party-id") Long partyId, @RequestBody PartyRequestDto partyRequestDtO) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_UPDATE_SUCCESS), OK);
    }

    @Operation(summary = "파티 상세", description = "파티의 상세 정보입니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyReadResponseDto.class)))
    @GetMapping("/{party-id}")
    public ResponseEntity readParty(@PathVariable("party-id") Long partyId){
        List<String> hashtags = Arrays.asList("BHC, 치킨");
        PartyReadResponseDto partyReadResponseDto = new PartyReadResponseDto("BHC 7시", "연희동 올리브영 앞에서 나눠요", LocalDateTime.now(),
                "연히동", 3, 4, "마감 임박",
                "연희동 주민", 10, hashtags);
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_READ_SUCCESS, partyReadResponseDto), OK);
    }

    @Operation(summary = "파티 삭제", description = "파티를 삭제합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{party-id}")
    public ResponseEntity deleteParty(@PathVariable("party-id") Long partyId) {
        partyService.deleteParty(partyId);
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_DELETE_SUCCESS), OK);

    }

}
