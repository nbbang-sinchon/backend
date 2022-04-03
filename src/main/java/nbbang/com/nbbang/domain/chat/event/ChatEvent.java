package nbbang.com.nbbang.domain.chat.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ChatEvent {
    private String content;
}
