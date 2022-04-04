package nbbang.com.nbbang.domain.cache;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final MemberService memberService;
    private final PartyRepository partyRepository;

    @Cacheable(key="#memberId", value="memberCache")
    public MemberCache getMemberCache(Long memberId){
        Member member = memberService.findById(Long.valueOf(memberId));
        return new MemberCache(memberId, member.getNickname(), member.getAvatar());
    }

    @Cacheable(key="#partyId", value="partyMemberCache")
    public List<PartyMemberIdCache> getPartyMembersCacheByPartyId(Long partyId){
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException(PARTY_NOT_FOUND));
        List<PartyMemberIdCache> caches = party.getPartyMembers().stream().map(pm -> PartyMemberIdCache.createByPartyMember(pm)).collect(Collectors.toList());
        return caches;
    }

}
