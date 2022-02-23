package nbbang.com.nbbang.domain.member.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.controller.MemberResponseMessage;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.FileUpload.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

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
    public String uploadAndUpdateAvatar(Long memberId, MultipartFile imgFile) throws IOException {
        Member member = findById(memberId);
        String oldAvatarUrl = member.getAvatar();
        String oldFileName = oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1);

        if (oldFileName != null) {
            s3Uploader.delete("profile-images", oldFileName);
        }
        //String originalFileName = imgFile.getOriginalFilename();
        String savedFileName = UUID.randomUUID().toString();
        String avatarUrl = s3Uploader.upload(imgFile, "profile-images", savedFileName);

        member.updateMember(avatarUrl);
        return avatarUrl;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        //memberRepository.deleteById(memberId);
        Member member = findById(memberId);
        member.leaveMember();
    }


}
