package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.domain.SendStatus;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindResponseDto;
import nbbang.com.nbbang.domain.party.dto.single.response.PartyReadResponseDto;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

@Builder
public class BbangpanReadResponseDto {
    // 필요한 정보
/*    은행, 계좌번호, 배달비
    유저별(나인지, 파티장인지, 그냥인지) 닉네임, 금액, 송금여부,
    */

    private String bank;
    private String account;
    private Integer deliveryfee;

    private List<PartyMemberResponseDto> partyMemberResponseDtos;

    public static void createDto(String bank, String acount, List<PartyMember> partyMembers) {

        //partyMembers.stream().map(PartyMemberResponseDto::createDto).collect(Collectors.toList());



    }

}
