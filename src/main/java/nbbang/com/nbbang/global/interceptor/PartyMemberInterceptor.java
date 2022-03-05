package nbbang.com.nbbang.global.interceptor;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorDto;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyMemberInterceptor implements HandlerInterceptor {
    private final CurrentMember currentMember;
    private final PartyMemberValidator partyMemberValidator;
    private final PartyMemberValidatorService partyMemberValidatorService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        PartyMemberValidatorDto dto = partyMemberValidatorService.createByUriAndMemberId(request.getRequestURI(), currentMember.id());
        return partyMemberValidator.isPartyMember(dto);
    }
}