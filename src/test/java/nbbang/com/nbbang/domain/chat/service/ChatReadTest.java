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

    @Test
    void readMessageTest() {
        join(1L, 1L);
        join(1L, 4L);
        join(1L, 5L);
        join(1L, 6L);
        // SIX MEMBERS IN PARTY 1

    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        Long messageId = partyMemberService.joinParty(party, member);
    }
}