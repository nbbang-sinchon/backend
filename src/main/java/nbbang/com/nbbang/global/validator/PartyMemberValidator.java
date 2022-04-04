package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.cache.CacheService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import java.util.Optional;

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartyMemberValidator {

    private final CacheService cacheService;
    private final PartyRepository partyRepository;
    private final EntityManager em;

    public boolean validatePartyMember(PartyMemberValidatorDto dto) {
        return validatePartyMember(dto.getParty(), dto.getMember());
    }

    public boolean validateOwner(PartyMemberValidatorDto dto) {
        return validateOwner(dto.getParty(), dto.getMember());
    }

    public boolean validatePartyMember(Party party, Member member) {
        return validatePartyMember(party.getId(), member.getId());
    }

    public boolean validateOwner(Party party, Member member) {
        if(!(Optional.ofNullable(party.getOwner()).orElse(Member.builder().build()).equals(member))){
             throw new NotOwnerException();
        }
        return true;
    }

    public boolean validatePartyMember(Long partyId, Long memberId) {
        if(!cacheService.getPartyMembersCacheByPartyId(partyId).stream().anyMatch(mp -> mp.getMemberId().equals(memberId))){
            throw new NotPartyMemberException();
        }
        return true;
    }
}
