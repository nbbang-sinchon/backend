package nbbang.com.nbbang.global.interceptor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class CurrentMember {
    public Long id() {
        try {
            String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }
    }
}
