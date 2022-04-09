package nbbang.com.nbbang.global.security.context;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentMemberImpl implements CurrentMember {

    @Override
    public Long id() {
        try {
            String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }
    }

}
