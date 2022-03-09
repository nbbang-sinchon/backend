package nbbang.com.nbbang.global.config;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.interceptor.LoginInterceptor;
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

    private final LoginInterceptor interceptor;
    private final PartyMemberInterceptor partyMemberInterceptor;
    private final OwnerInterceptor ownerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);


       List<String> partyMemberInterceptorUrlPatterns = Arrays.asList("/chats/*", "/bread-board/*");
       List<String> ownerInterceptorUrlPatterns =
                Arrays.asList("/parties/*", "/bread-board/*/delivery-fee", "/bread-board/*/account");


        registry.addInterceptor(partyMemberInterceptor)
                .addPathPatterns(partyMemberInterceptorUrlPatterns)
                .excludePathPatterns(ownerInterceptorUrlPatterns);


        registry.addInterceptor(ownerInterceptor)
               .addPathPatterns(ownerInterceptorUrlPatterns);

    }
}
