package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.controller.PartyController;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyRequestDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class OnePartyServiceTest {

    @Autowired PartyService partyService;
    @Autowired PartyRepository partyRepository;
    @Autowired EntityManager em;

    @Test
    public void createParty(){
        // given
        PartyRequestDto partyDto = PartyRequestDto.builder().title("파티").place("sinchon").goalNumber(3).build();
        List<String> hashtagContents = Arrays.asList("old1", "old2");
        Party createbyDto = partyDto.createByDto();
        // when
        Long createdPartyId = partyService.createParty(createbyDto, hashtagContents);
        // then
        Party party1 = partyRepository.findById(createdPartyId).orElse(null);
        assertThat(party1).isEqualTo(createbyDto);
    }


    @Test
    void changeStatus() {
    }

    @Test
    void changeGoalNumber() {
    }

    @Test
    void isPartyOwnerOrMember() {
    }

    @Test
    void joinParty() {
    }

    @Test
    void exitParty() {
    }

    @Test
    void findParty() {
    }

    @Test
    void updateParty() {
    }

    @Test
    void closeParty() {
    }

    @Test
    void createHashtag() {
    }

    @Test
    void createHashtags() {
    }

    @Test
    void findHashtagContentsByParty() {
    }

    @Test
    void findNearAndSimilar() {
    }
}