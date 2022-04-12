package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestDto;
import nbbang.com.nbbang.domain.party.dto.many.PartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.response.*;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Parties", description = "여러 개 파티 조회")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ManyPartyController {

    private final ManyPartyService manyPartyService;
    private final CurrentMember currentMember;

    @Operation(summary = "여러 개 파티 리스트 조회, 로그인이 안된 유저는 isWishlist 가 false 로 표시됩니다.", description = "여러 개의 파티 리스트를 전송합니다. 기본값으로 10개의 파티를 조회합니다. json이 아닌 query parameter로 데이터를 전송해야 합니다. 예시 : http://15.165.132.250:8094/parties?places=YEONHUI&places=CHANGCHEON&showOngoing=true&search=BHC")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartyListResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 쿼리 파라미터를 올바르게 입력하세요.", content = @Content(mediaType = "application/json"))
    @GetMapping("/parties")
    public DefaultResponse findParties(@ParameterObject @Validated @ModelAttribute PartyListRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(ManyPartyResponseMessage.ILLEGAL_PARTY_LIST_REQUEST, bindingResult);
        }
        Page<Party> res = manyPartyService.findAllParties(requestDto.createPageRequest(), requestDto.createPartyListRequestFilterDto(), requestDto.getCursorId(), currentMember.id());
        PartyListResponseDto dto = PartyListResponseDto.createByEntity(res.getContent(), currentMember.id());
        return DefaultResponse.res(StatusCode.OK, PartyResponseMessage.PARTY_FIND_SUCCESS, dto);
    }

}
