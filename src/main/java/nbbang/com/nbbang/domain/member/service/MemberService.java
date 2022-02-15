package nbbang.com.nbbang.domain.member.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.global.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveMember(String nickname, Place place) {
        Member member = Member.createMember(nickname, place);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * Member 조회하는 기능, MemberNotFoundException 을 throw 할 수 있음
     * @param memberId
     * @return
     */
    public Member findById(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void updateMember(Long memberId, String nickname, Place place) {
        Member member = findById(memberId);
        member.setNickname(nickname);
        member.setPlace(place);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        //memberRepository.deleteById(memberId);
        Member member = findById(memberId);
        member.setLeave(true);
    }


}
