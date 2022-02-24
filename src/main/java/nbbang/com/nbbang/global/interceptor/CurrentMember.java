package nbbang.com.nbbang.global.interceptor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
public class CurrentMember {
    private Long memberId;
    public Long id() {
        return memberId;
    }
}
