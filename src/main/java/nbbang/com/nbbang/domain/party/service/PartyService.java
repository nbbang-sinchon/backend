package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.MemberParty;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final HashtagService hashtagService;

    @Transactional
    public Long createParty(Party party, List<String> hashtags) {
        Party savedParty = partyRepository.save(party);
        Long partyId = savedParty.getId();
        if (!hashtags.isEmpty()){
            for (String content: hashtags){
                hashtagService.createHashtag(partyId, content);
            }
        }
        return partyId;
    }

    public Party findParty(Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException("정보가 일치하는 파티가 존재하지 않습니다."));
        return party;
    }


    @Transactional
    public void deleteParty(Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException("There is no party"));
        partyRepository.delete(party);
    }

    public List<String> findHashtagContentsByParty(Party party) {
        List<String> hashtagContents = party.getPartyHashtags().stream()
                .map(partyHashtag -> partyHashtag.getHashtag().getContent())
                .collect(Collectors.toList());
        return hashtagContents;
    }
}
