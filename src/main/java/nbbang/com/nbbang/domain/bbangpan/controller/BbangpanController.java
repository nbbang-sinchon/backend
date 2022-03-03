package nbbang.com.nbbang.domain.bbangpan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.dto.*;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.GlobalErrorResponseMessage;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;


@Tag(name = "Bbangpan", description = "빵판과 관련된 API입니다.(구현중)")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbangpans/{party-id}")
public class BbangpanController {

    private final PartyService partyService;
    private final PartyMemberService partyMemberService;
    private final CurrentMember currentMember;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Operation(summary = "빵판 정보", description = "유저가 빵판을 클릭했을 때, 필요한 정보를 보냅니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BbangpanReadResponseDto.class)))
    @GetMapping
    public DefaultResponse readBbangpan(@PathVariable("party-id") Long partyId) {
        Party party = partyService.findById(partyId);
        BbangpanReadResponseDto bbangpanReadResponseDto = BbangpanReadResponseDto.createDtoByParty(party);
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.BBANGPAN_READ_SUCCESS, bbangpanReadResponseDto);
    }

    @Operation(summary = "주문 금액 설정", description = "유저가 주문 금액을 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/price")
    public DefaultResponse changePrice(@PathVariable("party-id") Long partyId, @Valid @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        Integer price = bbangpanPriceChangeRequestDto.getPrice();
        partyMemberService.changePrice(partyId, currentMember.id(), price);
        BbangpanPriceChangeSocketDto bbangpanPriceChangeSocketDto = BbangpanPriceChangeSocketDto.createDto(currentMember.id(), price);
        simpMessagingTemplate.convertAndSend("/bbangpan/" + partyId, bbangpanPriceChangeSocketDto);
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.PRICE_CHANGE_SUCCESS);
    }

    @Operation(summary = "배달비 설정", description = "방장이 배달비를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PostMapping("/delivery-fee")
    public DefaultResponse changeDeliveryFee(@PathVariable("party-id") Long partyId, @Valid @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        Integer deliveryFee = bbangpanPriceChangeRequestDto.getPrice();
        partyService.changeDeliveryFee(partyId, currentMember.id(), deliveryFee);
        BbangpanDeliveryFeeChangeSocketDto bbangpanDeliveryFeeChangeSocketDto = BbangpanDeliveryFeeChangeSocketDto.createDto(deliveryFee);
        simpMessagingTemplate.convertAndSend("/bbangpan/" + partyId, bbangpanDeliveryFeeChangeSocketDto);
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.DELIVERYFEE_CHANGE_SUCCESS);
    }

    @Operation(summary = "송금 상태 설정", description = "송금 상태를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/send-status")
    public ResponseEntity changeSendStatus(@PathVariable("party-id") Long partyId, @RequestBody BbangpanStatusChangeRequestDto bbangpanStatusChangeRequestDto) {
        return new ResponseEntity(DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.SENDSTATUS_CHANGE_SUCCESS), OK);

    }

}
