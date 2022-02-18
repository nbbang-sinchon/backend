package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.MemberParty;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyHashtag;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.PartyRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyUpdateServiceDto;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
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
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final PartyHashtagRepository partyHashtagRepository;
    private final HashtagService hashtagService;

    @Transactional
    public Long createParty(Party party, List<String> hashtagContents) {
        Party savedParty = partyRepository.save(party);
        savedParty.changeStatus(PartyStatus.ON);
        Long partyId = savedParty.getId();
        hashtagContents.stream().forEach(content->createHashtag(partyId, content));
        return partyId;
    }

    public Party findParty(Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException(PARTY_NOT_FOUND));
        return party;
    }
    
    @Transactional
    public void updateParty(Long partyId, PartyUpdateServiceDto partyUpdateServiceDto) {
        Party party = findParty(partyId);
        party.update(partyUpdateServiceDto);
        if (partyUpdateServiceDto.getHashtagContents().isPresent()) {
            List<String> oldHashtagContents = findHashtagContentsByParty(party);
            List<String> newHashtagContents = partyUpdateServiceDto.getHashtagContents().get();
            oldHashtagContents.removeAll(newHashtagContents);
            newHashtagContents.removeAll(findHashtagContentsByParty(party));

            oldHashtagContents.stream().forEach(content -> deleteHashtag(partyId, content));
            newHashtagContents.stream().forEach(content -> createHashtag(partyId, content));

        }
    }

    @Transactional
    public void deleteParty(Long partyId) {
        Party party = findParty(partyId);
        partyRepository.delete(party);
        partyHashtagRepository.deleteAll(party.getPartyHashtags());
        party.getPartyHashtags().stream().forEach(partyHashtag -> hashtagService.deleteIfNotReferred(partyHashtag.getHashtag()));
    }

    @Transactional
    public void createHashtag(Long partyId, String content){
        Party party = findParty(partyId);
        Hashtag hashtag = hashtagService.findOrCreateByContent(content);
        PartyHashtag.createPartyHashtag(party, hashtag);
    }

    @Transactional // ************** 구현 필요(쿼리 최적화) ************** /
    public void createHashtags(Long partyId, List<String> hashtagContents){
        Party party = findParty(partyId);
        List<Hashtag> hashtags = hashtagService.findOrCreateByContent(hashtagContents);
        PartyHashtag.createPartyHashtags(party, hashtags);
    }

    private void deleteHashtag(Long partyId, String content) {
        Party party = findParty(partyId);
        PartyHashtag partyHashtag = party.deletePartyHashtag(content);
        partyHashtagRepository.delete(partyHashtag);
        hashtagService.deleteIfNotReferred(partyHashtag.getHashtag());
    }

    public List<String> findHashtagContentsByParty(Party party) {
        List<String> hashtagContents = party.getPartyHashtags().stream()
                .map(partyHashtag -> partyHashtag.getHashtag().getContent())
                .collect(Collectors.toList());
        return hashtagContents;
    }

    // 현재 Near, ON, 스스로 아님만 구현. Hashtag로 찾는 기능 추가하기.
    public List<Party> findNearAndSimilar(Long partyId) {
        Party party = findParty(partyId);
        Place place = party.getPlace();
        List<Party> parties = partyRepository.findByPlaceAndNotSelf(partyId);
        return parties;
    }
}
