package nbbang.com.nbbang.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    BLOCKED("ROLE_BLOCKED", "밴된 사용자"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
