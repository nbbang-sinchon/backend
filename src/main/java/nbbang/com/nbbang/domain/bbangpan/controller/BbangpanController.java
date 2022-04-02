package nbbang.com.nbbang.domain.bbangpan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.dto.*;
import nbbang.com.nbbang.domain.bbangpan.dto.request.BbangpanAccountChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.request.BbangpanPriceChangeRequestDto;
import nbbang.com.nbbang.domain.bbangpan.dto.request.SendStatusChangeRequestDto;
import nbbang.com.nbbang.domain.party.controller.PartyResponseMessage;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.GlobalErrorResponseMessage;
import nbbang.com.nbbang.global.error.exception.CustomIllegalArgumentException;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.*;
import nbbang.com.nbbang.global.socket.SocketSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;


@Tag(name = "BreadBoard", description = "빵판과 관련된 API입니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json"))
})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bread-board/{party-id}")
public class BbangpanController {

    private final PartyService partyService;
    private final PartyMemberService partyMemberService;
    private final CurrentMember currentMember;
    private final SocketSender socketSender;

    @Operation(summary = "빵판 정보", description = "유저가 빵판을 클릭했을 때, 필요한 정보를 보냅니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BbangpanResponseDto.class)))
    @GetMapping
    public DefaultResponse readBbangpan(@PathVariable("party-id") Long partyId) {
        Party party = partyService.findById(partyId);
        BbangpanResponseDto bbangpanResponseDto = BbangpanResponseDto.createDtoByParty(party);
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.BBANGPAN_READ_SUCCESS, bbangpanResponseDto);
    }

    @Operation(summary = "배달비 설정", description = "방장이 배달비를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PostMapping("/delivery-fee")
    public DefaultResponse changeDeliveryFee(@PathVariable("party-id") Long partyId, @Valid @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto, BindingResult bindingResult) throws NoSuchFieldException {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }

        changePartyField(partyId, currentMember.id(), Party.getField("deliveryFee"), bbangpanPriceChangeRequestDto.getPrice());
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.DELIVERYFEE_CHANGE_SUCCESS);
    }

    @Operation(summary = "계좌번호 설정", description = "방장이 계좌 번호를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Owner", content = @Content(mediaType = "application/json"))
    @PostMapping("/account")
    public DefaultResponse changeAccount(@PathVariable("party-id") Long partyId, @Valid @RequestBody BbangpanAccountChangeRequestDto bbangpanAccountChangeRequestDto, BindingResult bindingResult) throws NoSuchFieldException {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        changePartyField(partyId, currentMember.id(), Party.getField("accountNumber"), bbangpanAccountChangeRequestDto.getAccount());
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.ACCOUNT_CHANGE_SUCCESS);
    }

    public void changePartyField(Long partyId, Long memberId, Field field, Object value) throws NoSuchFieldException {
        partyService.changeField(partyId, memberId, field, value);
        sendSocket(partyId);
    }

    @Operation(summary = "주문 금액 설정", description = "유저가 주문 금액을 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/price")
    public DefaultResponse changePrice(@PathVariable("party-id") Long partyId, @Valid @RequestBody BbangpanPriceChangeRequestDto bbangpanPriceChangeRequestDto, BindingResult bindingResult) throws NoSuchFieldException {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(GlobalErrorResponseMessage.ILLEGAL_ARGUMENT_ERROR, bindingResult);
        }
        changePartyMemberField(partyId, currentMember.id(), PartyMember.getField("price"), bbangpanPriceChangeRequestDto.getPrice());
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.PRICE_CHANGE_SUCCESS);
    }

    @Operation(summary = "송금 상태 설정", description = "송금 상태를 설정합니다.")
    @ApiResponse(responseCode = "403", description = "Not Party Member", content = @Content(mediaType = "application/json"))
    @PostMapping("/send-status")
    public DefaultResponse changeSendStatus(@PathVariable("party-id") Long partyId, @Valid @RequestBody SendStatusChangeRequestDto sendStatusChangeRequestDto, BindingResult bindingResult) throws NoSuchFieldException {
        if (bindingResult.hasErrors()) {
            throw new CustomIllegalArgumentException(PartyResponseMessage.ILLEGAL_PARTY_STATUS, bindingResult);
        }
        changePartyMemberField(partyId, currentMember.id(), PartyMember.getField("isSent"), sendStatusChangeRequestDto.getIsSent());
        return DefaultResponse.res(StatusCode.OK, BbangpanResponseMessage.SENDSTATUS_CHANGE_SUCCESS);
    }

    public void changePartyMemberField(Long partyId, Long memberId, Field field, Object value) throws NoSuchFieldException {
        partyMemberService.changeField(partyId, memberId, field, value);
        sendSocket(partyId);
    }

    public void sendSocket(Long partyId){
        Party party = partyService.findById(partyId);
        BbangpanResponseDto bbangpanResponseDto = BbangpanResponseDto.createDtoByParty(party);
        socketSender.sendBreadBoard(partyId, bbangpanResponseDto);
    }
}
