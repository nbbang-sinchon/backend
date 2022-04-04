package nbbang.com.nbbang.domain.cache;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static nbbang.com.nbbang.domain.member.controller.MemberResponseMessage.MEMBER_NOT_FOUND;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;

    @Cacheable(key="#memberId", value="memberCache")
    public MemberCache getMemberCache(Long memberId){
        Member member = memberRepository.findById(Long.valueOf(memberId)).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
        return new MemberCache(memberId, member.getNickname(), member.getAvatar());
    }

    @CacheEvict(key="#memberId", value="memberCache")
    public void evictMemberCache(Long memberId){}

    @Cacheable(key="#partyId", value="partyMemberCache")
    public List<PartyMemberIdCache> getPartyMembersCacheByPartyId(Long partyId){
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException(PARTY_NOT_FOUND));
        List<PartyMemberIdCache> caches = party.getPartyMembers().stream().map(pm -> PartyMemberIdCache.createByPartyMember(pm)).collect(Collectors.toList());
        return caches;
    }

    @CacheEvict(key="#partyId", value="partyMemberCache")
    public void evictPartyMemberCache(Long partyId){}


}
