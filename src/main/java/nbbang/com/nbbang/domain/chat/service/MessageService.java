package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.CacheService;
import nbbang.com.nbbang.domain.chat.domain.MemberCache;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.test.BoardRepository;
import nbbang.com.nbbang.domain.test.BoardService;
import nbbang.com.nbbang.global.FileUpload.FileUploadService;
import nbbang.com.nbbang.global.socket.SocketSender;
import nbbang.com.nbbang.global.socket.service.SocketPartyMemberService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage.MESSAGE_NOT_FOUND;
import static nbbang.com.nbbang.domain.chat.domain.MessageType.IMAGE;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;
import static nbbang.com.nbbang.global.FileUpload.UploadDirName.DIR_CHATS;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final PartyRepository partyRepository;
    private final FileUploadService fileUploadService;
    private final SocketPartyMemberService socketPartyMemberService;
    private final EntityManager em;
    private final PartyMemberValidator partyMemberValidator;
    private final MemberService memberService;
    private final CacheService cacheService;

    @Transactional
    public Message send(Long partyId, Long senderId, String content) {
        return send(partyId, senderId, content,MessageType.CHAT);
    }

    @Transactional
    public Message send(Long partyId, Long senderId, String content, MessageType type) {
        Party party = partyRepository.findById(partyId).orElseThrow(()->new NotFoundException(PARTY_NOT_FOUND));
        Member senderProxy = em.getReference(Member.class, senderId);
        partyMemberValidator.validatePartyMember(party, senderProxy);
        MemberCache memberCache = cacheService.getMemberCache(senderId);
        Member sender = memberCache.createMember();
        Integer notReadNumber = getNotActiveNumber(party);
        Message message =Message.createMessage(sender, party, content, type, notReadNumber);
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }

    public Integer getNotActiveNumber(Party party) {
        Integer activeNumber = socketPartyMemberService.getPartyActiveNumber(party.getId());
        Integer partyMemberNumber = party.countPartyMemberNumber();
        return partyMemberNumber - activeNumber;
    }


    public Message findById(Long id){
        return messageRepository.findById(id).orElseThrow(()->new NotFoundException(MESSAGE_NOT_FOUND));
    }

    @Transactional
    public Message sendImage(Long partyId, Long senderId, MultipartFile imgFile) throws IOException {

        String uploadUrl = fileUploadService.upload(imgFile, DIR_CHATS);
        return send(partyId, senderId, uploadUrl,IMAGE);
    }

}
