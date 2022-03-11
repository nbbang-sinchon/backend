package nbbang.com.nbbang.domain.chat.service;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.UUID;

import static nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage.MESSAGE_NOT_FOUND;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;


public interface MessageService {

    public Message send(Long partyId, Long senderId, String content);

    public Long send(Long partyId, Long senderId, String content, MessageType type);

    public Message findById(Long id);

    public Message sendImage(Long partyId, Long senderId, MultipartFile imgFile) throws IOException;
}
