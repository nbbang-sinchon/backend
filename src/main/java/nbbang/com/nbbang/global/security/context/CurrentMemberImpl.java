package nbbang.com.nbbang.global.security.context;

import lombok.NoArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.AUTHENTICATION_ERROR;

@Component
@NoArgsConstructor
public class CurrentMemberImpl implements CurrentMember {

    private String attr = "memberId";

    @Override
    public Long id() {
        try {
            String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long idFromSocket(Message<?> message) {
        Map<String, Object> sessionHeaders = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
        return Long.parseLong(sessionHeaders.get(attr).toString());
    }

    @Override
    public void rememberIdSocket(ServerHttpRequest request, Map<String, Object> attributes) {
        try {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            attributes.put(attr, servletRequest.getPrincipal().getName());
        } catch (Exception e) {
            throw new IllegalStateException(AUTHENTICATION_ERROR);
        }
    }
}
