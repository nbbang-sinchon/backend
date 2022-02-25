package nbbang.com.nbbang.global.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentMember {
    private Long memberId;
    public Long id() {
        return memberId;
    }
}
