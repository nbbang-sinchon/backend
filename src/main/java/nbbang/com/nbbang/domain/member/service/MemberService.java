package nbbang.com.nbbang.domain.member.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.partymember.repository.PartyMemberRepository;
import nbbang.com.nbbang.global.cache.MemberCacheService;
import nbbang.com.nbbang.domain.member.controller.MemberResponseMessage;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.global.FileUpload.FileUploadService;
import nbbang.com.nbbang.global.security.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;

import static nbbang.com.nbbang.global.FileUpload.UploadDirName.DIR_AVATAR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileUploadService fileUploadService;
    private final PartyMemberRepository partyMemberRepository;
    private final MemberCacheService memberCacheService;

    @Transactional
    @Deprecated
    public Long saveMember(String nickname, Place place) {
        Member member = Member.createMember(nickname, place);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    @Deprecated
    public Long saveMember(String nickname, Place place, String email, Role role) {
        Member member = Member.builder().nickname(nickname).place(place).email(email).role(role).build();
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
        memberCacheService.evictMemberCache(memberId);
    }

    @Transactional
    public String uploadAndUpdateAvatar(Long memberId, MultipartFile imgFile) throws IOException {
        Member member = findById(memberId);
        memberCacheService.evictMemberCache(memberId);
        return member.uploadAvatar(fileUploadService.deleteAndUpload(member.getAvatar(), imgFile, DIR_AVATAR));
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findById(memberId);
        member.leaveMember();
    }

    public Boolean isThereNotReadChat(Long memberId) {
        return partyMemberRepository.isThereNotReadMessageByMemberId(memberId);
    }

}
