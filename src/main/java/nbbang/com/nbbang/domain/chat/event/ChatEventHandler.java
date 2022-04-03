package nbbang.com.nbbang.domain.chat.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatEventHandler {

    @EventListener
    @Async
    public void handle(ChatEvent event){
        log.info("chat even handler******");
        log.info(String.valueOf(Thread.currentThread().hashCode()));
        log.info("채팅 이벤트 수신: {}",event.getContent());
        log.info("chat even handler******");
    }
}
