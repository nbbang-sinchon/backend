package nbbang.com.nbbang.domain.chat.domain;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.test.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {

    @Autowired
    MemberService memberService;

    @Cacheable(key="#memberId", value="getMemberCache")
    public MemberCache getMemberCache(Long memberId){
        Member member = memberService.findById(Long.valueOf(memberId));
        return new MemberCache(memberId, member.getNickname(), member.getAvatar());
    }

}
