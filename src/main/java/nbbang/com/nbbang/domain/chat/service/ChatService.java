package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;

    public Long findLastMessageId(Long partyId) {
        return messageRepository.findLastMessageId(partyId);
    }
}
