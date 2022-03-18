package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyRequestDto;
import nbbang.com.nbbang.domain.party.dto.single.PartyUpdateServiceDto;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nbbang.com.nbbang.domain.member.dto.Place.SINCHON;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SinglePartyServiceTest {

    @Autowired PartyService partyService;
    @Autowired PartyRepository partyRepository;
    @Autowired EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void createParty(){
        // given
        Member member = Member.builder().nickname("test member").build();
        Member saveMember = memberRepository.save(member);
        List<String> hashtagContents = Arrays.asList("old1", "old2");
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        // when
        Party createdParty = partyService.create(party, saveMember.getId(), hashtagContents);
        // then
        Party findParty = partyRepository.findById(createdParty.getId()).orElse(null);
        assertThat(findParty).isEqualTo(party);
    }


    @Test
    void findParty() {
        // given
        Member member = Member.builder().nickname("test member").build();
        Member saveMember = memberRepository.save(member);
        List<String> hashtagContents = Arrays.asList("BHC", "bhc");
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        Party createdParty = partyService.create(party, saveMember.getId(), hashtagContents);
        // when
        Party findParty = partyService.findById(createdParty.getId());

        // then
        assertThat(findParty).isEqualTo(party);
        assertThat(findParty.getPartyHashtags().size()).isEqualTo(2);
        assertThat(findParty.getHashtagContents().contains("bhc"));
    }

    /*@Test
    void updateParty() {
        // given
        List<String> hashtagContents = Arrays.asList("old1", "old2");
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        Long createdPartyId = partyService.create(party, hashtagContents);
        PartyRequestDto partyRequestDto = PartyRequestDto.builder().title("new title").content("hello world!").hashtags(new ArrayList<>(Arrays.asList("old1", "new1"))).build();
        // https://kkwonsy.tistory.com/14
        // when
        Long updatePartyId = partyService.update(createdPartyId, PartyUpdateServiceDto.createByPartyRequestDto(partyRequestDto));
        // then
        Party updatedParty = partyService.findById(updatePartyId);
        assertThat(updatedParty.getTitle()).isEqualTo("new title");
        assertThat(updatedParty.getContent()).isEqualTo("hello world!");
        assertThat(updatedParty.getHashtagContents()).contains("old1", "new1");
    }*/

    @Test
    void findNearAndSimilar() {
        Member member = Member.builder().nickname("test member").build();
        Member saveMember = memberRepository.save(member);
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        Party createdParty = partyService.create(party, saveMember.getId(), null);

        // when
        List<Party> findParties = partyService.findNearAndSimilar(createdParty.getId());
        // then
        assertThat(findParties.size()).isLessThanOrEqualTo(6);
        findParties.stream().forEach(findParty->assertThat(findParty.getPlace()).isEqualTo(party.getPlace()));
        findParties.stream().forEach(findParty->assertThat(findParty.getStatus()).isEqualTo(PartyStatus.OPEN));

    }
}