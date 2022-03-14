package nbbang.com.nbbang.domain.bbangpan.repository;

import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyMemberRepositoryTest {
    @Autowired
    PartyMemberRepository memberPartyRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired
    MessageRepository messageRepository;

    @Test
    public void memberPartySaveAndFindTest() {
        // given
        Member member = Member.builder().nickname("member").build();
        memberRepository.save(member);
        Party party = Party.builder().owner(member).goalNumber(10).status(PartyStatus.OPEN).title("party").build();
        partyRepository.save(party);
        PartyMember savePartyMember = PartyMember.createMemberParty(member, party, messageRepository.findLastMessage(party.getId()));
        memberPartyRepository.save(savePartyMember);
        // when
        PartyMember findPartyMember = memberPartyRepository.findByMemberIdAndPartyId(member.getId(), party.getId());
        // then
        assertThat(savePartyMember).isEqualTo(findPartyMember);
    }
}