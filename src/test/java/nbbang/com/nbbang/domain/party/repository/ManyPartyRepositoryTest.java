package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.many.PartyFindRequestFilterDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ManyPartyRepositoryTest {
    @Autowired PartyRepository partyRepository;
    @Autowired ManyPartyRepository manyPartyRepository;

    @Test
    public void filterPartyByTitle() {
        // given
        Party party1 = Party.builder().title("뿌링클 오늘 7시").build();
        Party party2 = Party.builder().title("뿌링클 내일 7시").build();
        Party party3 = Party.builder().title("뿌뿌뿌 오늘 7시").build();
        partyRepository.saveAll(Arrays.asList(party1, party2, party3));

        PartyFindRequestFilterDto filter = PartyFindRequestFilterDto.createRequestFilterDto("뿌뿌뿌");

        // when
        Page<Party> res = manyPartyRepository.findAllByRequestDto(PageRequest.of(0, 10), filter);
        // then
        Assertions.assertThat(res.getContent().size()).isEqualTo(1);
    }

    @Test
    public void filterPartyByStatus() {
        // given
        Party party1 = Party.builder().title("뿌링").status(PartyStatus.OPEN).build();
        Party party2 = Party.builder().title("뿌링").status(PartyStatus.CLOSED).build();
        Party party3 = Party.builder().title("뿌링").status(PartyStatus.FULL).build();
        partyRepository.saveAll(Arrays.asList(party1, party2, party3));

        PartyFindRequestFilterDto filter = PartyFindRequestFilterDto.createRequestFilterDto(true, null);

        // when
        Page<Party> res = manyPartyRepository.findAllByRequestDto(PageRequest.of(0, 10), filter);
        // then
        Assertions.assertThat(res.getContent().size()).isEqualTo(1);
    }
}