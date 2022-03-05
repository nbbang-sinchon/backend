package nbbang.com.nbbang.global.handler;
// https://www.youtube.com/watch?v=gQyRxPjssWg


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ReplyEchoHandler extends TextWebSocketHandler {
    // Map<WebSocketSession, Long> sessions = new ArrayList<>();
    Map<String, WebSocketSession> userSessions = new HashMap<>();
    // 연결된 세션의 정보 받음

    @Override // 커넥션이 연결됐을 때
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("afterConnectionEstablished, {}",session);
        String senderId = session.getId();// 웹소켓의 id
        userSessions.put(senderId, session); // 로그인한 유저의 id
    }

    @Override // 서버에서 메시지 수신. 거의 안쓸듯!
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        // 접속된 모든 유저에 공지
        String senderId = session.getId(); // 웹소켓의 id
        // protocol: cmd, 댓글 작성자, 게시글 작성자, bno (reply, user2, user1, 234)
        String msg = message.getPayload().toString();
        if(StringUtils.isNotEmpty(msg)) {
            String[] strs = msg.split(",");
            if (strs!= null && strs.length ==4){
                String cmd = strs[0];
                String replyWriter = strs[1];
                String boardWriter = strs[2];
                String bno = strs[3];
                WebSocketSession boardWriterSession = userSessions.get(boardWriter);
                if("reply".equals(cmd) && boardWriterSession!=null){
                    boardWriterSession.sendMessage(new TextMessage(replyWriter+"님이 "+ bno+"번 게시글에 댓글을 달았습니다."));
                }
            }
        }


    }

    private String getId(WebSocketSession session) {
        // 로그인한 유저의 id를 찾아야 하는데 로그인 후 구현하기
/*        Map<String, Object> HttpSession = session.getAttributes();
        httpSession.get(SessionNames.LOGIN);
        if(null==loginUse)*/
        return "hello";
    }

    @Override // 커넥션이 끊겼을 때
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
