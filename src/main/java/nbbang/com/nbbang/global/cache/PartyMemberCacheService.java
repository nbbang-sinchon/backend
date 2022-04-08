package nbbang.com.nbbang.global.cache;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static nbbang.com.nbbang.domain.member.controller.MemberResponseMessage.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyMemberCacheService {


    private final PartyRepository partyRepository;




    @Cacheable(key="#partyId", value="partyMemberCache")
    public List<PartyMemberIdCache> getPartyMembersCacheByPartyId(Long partyId){
        Party party = partyRepository.findWithPartyMember(partyId);
        return getPartyMemberIdCaches(party);
    }

    @Cacheable(key="#party.id", value="partyMemberCache")
    public List<PartyMemberIdCache> getPartyMembersCacheByParty(Party party){
        return getPartyMemberIdCaches(party);
    }

    private List<PartyMemberIdCache> getPartyMemberIdCaches(Party party){
        List<PartyMemberIdCache> caches = party.getPartyMembers().stream().map(pm -> PartyMemberIdCache.createByPartyMember(pm)).collect(Collectors.toList());
        return caches;
    }

    @CacheEvict(key="#partyId", value="partyMemberCache")
    public void evictPartyMemberCache(Long partyId){}


}
