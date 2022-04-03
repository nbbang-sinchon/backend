package nbbang.com.nbbang.domain.chat.event;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableAsync
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish() throws Exception {
        applicationEventPublisher.publishEvent(new ChatEvent("chats!"));
    }
}
