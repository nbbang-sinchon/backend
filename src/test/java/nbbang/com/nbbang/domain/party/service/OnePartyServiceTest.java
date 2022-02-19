package nbbang.com.nbbang.domain.party.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class OnePartyServiceTest {

    @Autowired
    public PartyService partyService;

    @Test
    void createParty() {

        // partyService.createParty();
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