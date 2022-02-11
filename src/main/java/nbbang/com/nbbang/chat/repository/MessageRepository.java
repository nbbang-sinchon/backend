package nbbang.com.nbbang.chat.repository;

import nbbang.com.nbbang.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
