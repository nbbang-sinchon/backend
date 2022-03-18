package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.many.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import nbbang.com.nbbang.domain.party.repository.ManyPartyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class ManyPartyService {
    private final ManyPartyRepository manyPartyRepository;
    private final MessageRepository messageRepository;

    public Page<Party> findAllParties(Pageable pageable, Boolean isMyParties, PartyListRequestFilterDto filter, Long cursorId, Long memberId, List<String> hashtags, Long ... partyId) {
        return manyPartyRepository.findAllParties(pageable, isMyParties, filter, cursorId, memberId, hashtags, partyId);
    }
}
