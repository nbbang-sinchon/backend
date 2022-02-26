package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.ChatSession;
import nbbang.com.nbbang.domain.chat.repository.ChatSessionRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final PartyService partyService;

    @Transactional
    public void createBySessionIdAndPartyId(String sessionId, Long partyId){
        ChatSession chatSession = ChatSession.builder().sessionId(sessionId).party(partyService.findById(partyId)).build();
        chatSessionRepository.save(chatSession);
    }

    @Transactional
    public Long deleteIfExistBySessionId(String sessionId) {
        ChatSession chatSession = chatSessionRepository.findBySessionId(sessionId);
        Long partyId = -1L;
        if(chatSession!=null){
            chatSessionRepository.delete(chatSession);
            partyId = chatSession.getParty().removeChatSession(chatSession);
        }
        return partyId;
    }
}
