package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2MemberService customOAuth2MemberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        //StaticHeadersWriter writer = new StaticHeadersWriter("Access-Control-Allow-Headers", "Origin, origin, x-requested-with, authorization, " +
        //        "Content-Type, Authorization, credential, X-XSRF-TOKEN");
        http
                .addFilterBefore(encodingFilter, WebAsyncManagerIntegrationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable().cors()
                .and()
                    .headers().frameOptions().disable()
                    //.addHeaderWriter(writer)
                .and()
                    .oauth2Login()
                    //.defaultSuccessUrl("/members")
                //.defaultSuccessUrl("localhost")
                    .successHandler(new LoginSuccessHandler())
                    .failureUrl("/members/loginFail")
                .and()
                    .logout().logoutUrl("/logout")
                    .invalidateHttpSession(true)
                .and()
                    .cors();
        //super.configure(http);

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}