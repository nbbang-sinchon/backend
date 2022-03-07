package nbbang.com.nbbang.domain.bbangpan.domain;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BreadBoardPartyMemberService {

    private final PartyMemberRepository partyMemberRepository;

    @Transactional
    public PartyMember findByMemberIdAndPartyId(Long memberId, Long partyId){
        return partyMemberRepository.findByMemberIdAndPartyId(memberId, partyId);
    }
}
