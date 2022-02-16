package nbbang.com.nbbang.domain.chat.repository;

public interface MessageRepositorySupport {
    Long findLastMessageId(Long partyId);
}
