package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyHashtag;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.many.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ManyPartyServiceTest {
    @Autowired ManyPartyService manyPartyService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired HashtagRepository hashtagRepository;
    @Autowired PartyHashtagRepository partyHashtagRepository;

    @Test
    public void findAllPartiesTest1() {
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party party = Party.builder().place(Place.SINCHON).owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("party").build();
        partyRepository.save(party);
        Hashtag hashtag1 = Hashtag.builder().content("치킨").build();
        hashtagRepository.save(hashtag1);
        PartyHashtag ph = PartyHashtag.builder().party(party).hashtag(hashtag1).build();
        partyHashtagRepository.save(ph);
        party.addPartyHashtag(ph);
        Page<Party> parties = manyPartyService.findAllParties(PageRequest.of(0, 10), false, PartyListRequestFilterDto.builder().build(), null, memberA.getId(), null);
        Party findParty = parties.getContent().get(0);
        System.out.println(parties.getContent().get(0).getTitle());
        assertThat(findParty.getPartyHashtags().size()).isEqualTo(1);
        assertThat(parties.getContent().size()).isEqualTo(1);
    }

    /*@Test
    public void findLastPartyTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        for (int i = 0; i < 20; i++) {
            Party party = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("party " + i).build();
            partyRepository.save(party);
        }
        Party saveLastParty = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("last party").build();
        partyRepository.save(saveLastParty);
        // when
        Party findLastParty = manyPartyService.findLastParty();
        // then
        assertThat(saveLastParty.equals(findLastParty));
    }

    @Test
    public void findAllByCursoredFilterDtoTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        for (int i = 0; i < 20; i++) {
            Party party = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("party " + i).build();
            partyRepository.save(party);
        }
        Party saveCursorParty = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("target").build();
        partyRepository.save(saveCursorParty);
        for (int i = 21; i < 40; i++) {
            Party party = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("party " + i).build();
            partyRepository.save(party);
        }
        // when
        Page<Party> findPartyList = manyPartyService.findAllByCursoredFilterDto(PageRequest.of(0, 10), PartyFindRequestFilterDto.createRequestFilterDto(), saveCursorParty.getId());
        // then
        assertThat(findPartyList.getContent().get(0).equals(saveCursorParty));
    }

    @Test
    public void findAllByCursoredFilterDtoTest2() {
        // given
        String filterText = "party";
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        for (int i = 0; i < 20; i++) {
            Party party = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title(filterText + i).build();
            partyRepository.save(party);
        }

        for (int i = 21; i < 40; i++) {
            Party party = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("ppurinkle " + i).build();
            partyRepository.save(party);
        }
        Party cursor = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).title("target").build();
        partyRepository.save(cursor);
        // when
        Page<Party> findPartyList = manyPartyService.findAllByCursoredFilterDto(PageRequest.of(0, 10), PartyFindRequestFilterDto.createRequestFilterDto(filterText), cursor.getId());
        // then
        for (Party p : findPartyList.getContent()) {
            assertThat(p.getTitle().contains(filterText));
        }
    }*/









}