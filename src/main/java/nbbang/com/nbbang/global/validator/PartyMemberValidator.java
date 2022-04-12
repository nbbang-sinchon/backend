package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.cache.PartyMemberCacheService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.cache.PartyMemberIdCache;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartyMemberValidator {

    private final PartyMemberCacheService partyMemberCacheService;
    private final PartyRepository partyRepository;
    private final EntityManager em;


    public boolean validatePartyMember(Party party, Member member) {
        return validatePartyMember(party.getId(), member.getId());
    }

    public boolean validateOwner(Party party, Member member) {
        if(!(Optional.ofNullable(party.getOwner()).orElse(Member.builder().build()).equals(member))){
             throw new NotOwnerException();
        }
        return true;
    }

    public boolean validatePartyMember(Party party, Long memberId) {
        return validatePartyMember(party, null, memberId);
    }

    public boolean validatePartyMember(Long partyId, Long memberId) {
        return validatePartyMember(null, partyId, memberId);
    }

    private boolean validatePartyMember(Party party, Long partyId, Long memberId){
        List<PartyMemberIdCache> partyMemberIdCaches = (party!=null? partyMemberCacheService.getPartyMembersCacheByParty(party): partyMemberCacheService.getPartyMembersCacheByPartyId(partyId));
        if(!partyMemberIdCaches.stream().anyMatch(mp -> mp.getMemberId().equals(memberId))){
            throw new NotPartyMemberException();
        }
        return true;
    }
}
