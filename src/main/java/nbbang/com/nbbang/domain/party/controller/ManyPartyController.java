package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Status;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindResponseDto;
import nbbang.com.nbbang.domain.party.dto.PartyRequestDto;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Tag(name = "parties", description = "여러 개 파티 조회")
@RestController
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found")
})
@Slf4j
public class ManyPartyController {
    @Operation(summary = "여러 개 파티 리스트", description = "여러 개의 파티 리스트를 전송합니다.")
    @GetMapping("/parties")
    public Result findParty(@Validated @ModelAttribute PartyFindRequestDto partyFindRequestDto, BindingResult bindingResult) {
        log.info("errors={}", bindingResult);
        List<String> hashtags = new ArrayList<>();
        hashtags.add("BHC");
        hashtags.add("치킨");
        PartyFindResponseDto partyFindResponseDto = new PartyFindResponseDto("연희동 올리브영 앞 BHC 7시", LocalDateTime.now(), 3, 4, "마감 임박", hashtags);
        List<PartyFindResponseDto> collect = new ArrayList<>();
        collect.add(partyFindResponseDto);
        return new Result(collect);
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class Result<T>{ // 얘를 어디에 둘지 고민해보기
        private T parties;
    }

}