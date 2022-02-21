package nbbang.com.nbbang.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

// handler 파일 3개는 참고용으로 넣어둔 것이고, 저희 프로젝트에서는 stomp 방식을 사용하기 때문에 handler는 사용하지 않을 것 같습니다.
// https://dev-gorany.tistory.com/212
@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    // Client가 접속 시 호출되는 메서드 *//*

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);

        log.info(session + " 클라이언트 접속");
    }


     // Client가 접속 해제 시 호출되는 메서드드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}
