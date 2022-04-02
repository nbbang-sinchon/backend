package nbbang.com.nbbang.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.service.PartyWishlistService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.web.bind.annotation.*;

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.*;

@Tag(name = "Party Wishlist", description = "위시리스트에 파티를 추가 / 삭제합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
})
@Slf4j
@RestController
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyWishlistController {
    private final PartyWishlistService partyWishlistService;
    private final CurrentMember currentMember;

    @Operation(summary = "위시리스트에 파티 추가", description = "위시리스트에 파티를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = WISHLIST_DUPLICATE_ADD_ERROR, content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
    @PostMapping("/{party-id}/wishlist")
    public DefaultResponse addWishlist(@PathVariable("party-id") Long partyId) {
        partyWishlistService.addWishlistIfNotDuplicate(currentMember.id(), partyId);
        return DefaultResponse.res(StatusCode.OK, WISHLIST_ADD_SUCCESS);
    }

    @Operation(summary = "위시리스트에서 파티 삭제", description = "위시리스트에서 파티를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = WISHLIST_NOT_FOUND, content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{party-id}/wishlist")
    public DefaultResponse deleteWishlist(@PathVariable("party-id") Long partyId) {
        partyWishlistService.deleteWishlist(currentMember.id(), partyId);
        return DefaultResponse.res(StatusCode.OK, WISHLIST_DELETE_SUCCESS);
    }

}
