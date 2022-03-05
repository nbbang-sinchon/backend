package nbbang.com.nbbang.global.interceptor;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class PartyMemberInterceptor implements HandlerInterceptor {
    private final CurrentMember currentMember;
    private final PartyMemberValidator partyMemberValidator;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return partyMemberValidator.isPartyMember(request.getRequestURI(), currentMember.id());
    }
}