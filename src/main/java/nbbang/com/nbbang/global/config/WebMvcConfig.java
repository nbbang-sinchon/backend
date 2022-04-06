package nbbang.com.nbbang.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

// @Configuration // validator 기능을 서비스 단으로 옮깁니다.
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {


    // 적용해야할 api를 확인하는 용도로만 남겨둡니다.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> partyMemberInterceptorUrlPatterns = Arrays.asList("/chats/*", "/bread-board/*");
        List<String> ownerInterceptorUrlPatterns =
                Arrays.asList("/parties/*", "/bread-board/*/delivery-fee", "/bread-board/*/account");
        List<String> ownerInterceptorExcludeUrlPatterns =
                Arrays.asList("/parties/*/wishlist", "/parties/*/join", "/parties/*/exit");
    }
}
