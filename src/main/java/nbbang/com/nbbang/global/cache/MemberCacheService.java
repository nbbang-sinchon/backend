package nbbang.com.nbbang.global.cache;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import static nbbang.com.nbbang.domain.member.controller.MemberResponseMessage.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCacheService {

    private final MemberRepository memberRepository;

    @Cacheable(key="#memberId", value="memberCache")
    public MemberCache getMemberCache(Long memberId){
        Member member = memberRepository.findById(Long.valueOf(memberId)).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
        return new MemberCache(memberId, member.getNickname(), member.getAvatar());
    }

    @CacheEvict(key="#memberId", value="memberCache")
    public void evictMemberCache(Long memberId){}
}
