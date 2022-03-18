package nbbang.com.nbbang.global.config;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.interceptor.OwnerInterceptor;
import nbbang.com.nbbang.global.interceptor.PartyMemberInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final PartyMemberInterceptor partyMemberInterceptor;
    private final OwnerInterceptor ownerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> partyMemberInterceptorUrlPatterns = Arrays.asList("/chats/*", "/bread-board/*");
        List<String> ownerInterceptorUrlPatterns =
                Arrays.asList("/parties/*", "/bread-board/*/delivery-fee", "/bread-board/*/account");
        List<String> ownerInterceptorExcludeUrlPatterns =
                Arrays.asList("/parties/*/wishlist", "/parties/*/join", "/parties/*/exit");

        registry.addInterceptor(partyMemberInterceptor)
                .order(2)
                .addPathPatterns(partyMemberInterceptorUrlPatterns)
                .excludePathPatterns(ownerInterceptorUrlPatterns);

        registry.addInterceptor(ownerInterceptor)
                .order(3)
                .addPathPatterns(ownerInterceptorUrlPatterns)
                .excludePathPatterns(ownerInterceptorExcludeUrlPatterns);


    }
}
