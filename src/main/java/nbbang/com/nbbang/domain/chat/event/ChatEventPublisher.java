package nbbang.com.nbbang.domain.chat.event;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableAsync
public class ChatEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(Message message) {
        applicationEventPublisher.publishEvent(message);
    }
}
