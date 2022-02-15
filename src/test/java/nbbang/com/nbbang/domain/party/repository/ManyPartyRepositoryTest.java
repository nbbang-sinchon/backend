package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Part;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ManyPartyRepositoryTest {
    @Autowired PartyRepository partyRepository;
    @Autowired ManyPartyRepository manyPartyRepository;

    @Test
    public void filterTest() {
        // given
        Party party1 = Party.builder().title("뿌링클 오늘 7시").build();
        Party party2 = Party.builder().title("뿌링클 내일 7시").build();
        Party party3 = Party.builder().title("뿌뿌뿌 오늘 7시").build();
        partyRepository.saveAll(Arrays.asList(party1, party2, party3));

        PartyFindRequestFilterDto filter = PartyFindRequestFilterDto.createRequestFilterDto("뿌뿌뿌");
    }
}