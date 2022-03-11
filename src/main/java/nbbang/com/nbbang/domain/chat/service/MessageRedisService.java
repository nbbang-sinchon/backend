package nbbang.com.nbbang.domain.chat.service;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MessageRedisService implements MessageService{
    @Override
    public Message send(Long partyId, Long senderId, String content) {
        return null;
    }

    @Override
    public Long send(Long partyId, Long senderId, String content, MessageType type) {
        return null;
    }

    @Override
    public Message findById(Long id) {
        return null;
    }

    @Override
    public Message sendImage(Long partyId, Long senderId, MultipartFile imgFile) throws IOException {
        return null;
    }
}
