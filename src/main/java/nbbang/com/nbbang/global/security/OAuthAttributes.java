package nbbang.com.nbbang.global.security;

import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String avatar;

    private static String randomNickname() {
        String [] randomNickname = { "루피", "군침이 도는 루피", "요리공주 루피", "카레빵맨", "호빵맨", "식빵맨", "엔빵맨", "빵을 좋아하는 루피", "엔빵 루피", "엔빵 회원1" };
        int randomIdx = (int)(Math.random() * 10);
        return randomNickname[randomIdx];
    }

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String nickname,
                           String email, String avatar) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.avatar = avatar;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "kakao":
                return ofKakao(userNameAttributeName, attributes);
            case "naver":
                return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .avatar((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String)((Map)attributes.get("properties")).get("nickname"))
                .email(attributes.get("id").toString())
                .avatar((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map attrs = (Map)attributes.get("response");
        attrs.keySet().stream().forEach(k -> {
            System.out.print(k + " : ");
            System.out.println(attrs.get(k).toString());
        });

        return OAuthAttributes.builder()
                .nickname(randomNickname())
                .email((String) attrs.get("id"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
