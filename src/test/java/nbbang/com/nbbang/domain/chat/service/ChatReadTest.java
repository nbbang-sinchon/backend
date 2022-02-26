package nbbang.com.nbbang.domain.chat.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatReadTest {

    @Autowired ChatService chatService;
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired PartyMemberService partyMemberService;
    @Autowired MessageService messageService;

    @Test
    void readMessageTest() {

        Long partyId = 1L;
        join(partyId, 4L); // 파티 1번에는 1~4까지 4명이 존재함
        Long messageId = messageService.send(partyId, 3L, "hello");


    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        Long messageId = partyMemberService.joinParty(party, member);
    }
}