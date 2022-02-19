package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.controller.PartyController;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.PartyRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyUpdateServiceDto;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.response.DefaultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nbbang.com.nbbang.domain.member.dto.Place.SINCHON;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
// @Rollback(false)
class OnePartyServiceTest {

    @Autowired PartyService partyService;
    @Autowired PartyRepository partyRepository;
    @Autowired EntityManager em;

    @Test
    public void createParty(){
        // given
        List<String> hashtagContents = Arrays.asList("old1", "old2");
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        // when
        Long createdPartyId = partyService.createParty(party, hashtagContents);
        // then
        Party findParty = partyRepository.findById(createdPartyId).orElse(null);
        assertThat(findParty).isEqualTo(party);
    }


    @Test
    void findParty() {
        // given
        List<String> hashtagContents = Arrays.asList("old1", "old2");
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        Long createdPartyId = partyService.createParty(party, hashtagContents);
        // when
        Party findParty = partyService.findParty(createdPartyId);
        // then
        assertThat(findParty).isEqualTo(party);
    }

    @Test
    void updateParty() {
        // given
        List<String> hashtagContents = Arrays.asList("old1", "old2");
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        Long createdPartyId = partyService.createParty(party, hashtagContents);
        PartyRequestDto partyRequestDto = PartyRequestDto.builder().title("new title").content("hello world!").hashtags(new ArrayList<>(Arrays.asList("old1", "new1"))).build();
        // https://kkwonsy.tistory.com/14
        // when
        Long updatePartyId = partyService.updateParty(createdPartyId, PartyUpdateServiceDto.createByPartyRequestDto(partyRequestDto));
        // then
        Party updatedParty = partyService.findParty(updatePartyId);
        assertThat(updatedParty.getTitle()).isEqualTo("new title");
        assertThat(updatedParty.getContent()).isEqualTo("hello world!");
        assertThat(updatedParty.getHashtagContents()).contains("old1", "new1");
    }


    @Test
    void findNearAndSimilar() {
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(3).build();
        Long createdPartyId = partyService.createParty(party, null);

        // when
        List<Party> findParties = partyService.findNearAndSimilar(createdPartyId);
        // then
        assertThat(findParties.size()).isLessThanOrEqualTo(6);
        findParties.stream().forEach(findParty->assertThat(findParty.getPlace()).isEqualTo(party.getPlace()));
        findParties.stream().forEach(findParty->assertThat(findParty.getStatus()).isEqualTo(PartyStatus.OPEN));

    }
    // run - debug run all in test Ctrl+Shift+F10
}