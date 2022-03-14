package nbbang.com.nbbang.domain.chat.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatRoom implements Serializable {
    private static final long serialVersionUd = 646945368721L;
    private Long partyId;
    public static ChatRoom create(Long partyId){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.partyId = partyId;
        return chatRoom;
    }
}
