package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webjars.NotFoundException;

import static nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage.MESSAGE_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Long send(Long partyId, Long memberId, String content) {
        Long messageId = 3L;
        return messageId;
    }

    public Message findById(Long id){
        return messageRepository.findById(id).orElseThrow(()->new NotFoundException(MESSAGE_NOT_FOUND));
    }

}
