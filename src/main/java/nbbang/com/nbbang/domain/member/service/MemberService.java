package nbbang.com.nbbang.domain.member.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.controller.MemberResponseMessage;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

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
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(MemberResponseMessage.NOT_FOUND_MEMBER));
    }

    @Transactional
    public void updateMember(Long memberId, String nickname, Place place) {
        Member member = findById(memberId);
        member.updateMember(nickname, place);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        //memberRepository.deleteById(memberId);
        Member member = findById(memberId);
        member.leaveMember();
    }


}
