package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
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
        http
                .addFilterBefore(encodingFilter, WebAsyncManagerIntegrationFilter.class)
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .cors()
                .and()
                .headers().frameOptions().disable()
                .and()

                //.authorizeRequests()
                //.antMatchers(HttpMethod.GET, "/parties").permitAll()
                //.antMatchers("/login/oauth2/code/google").permitAll()
                //.antMatchers("/swagger-ui/**").permitAll()
                //.antMatchers("/**").permitAll()
                //.antMatchers("/*").permitAll()
                //.anyRequest().authenticated()
                //.and()
                .oauth2Login()
                .defaultSuccessUrl("/members")
                .failureUrl("/members/loginFail")
                .and()
                .logout().logoutUrl("/logout")
                .invalidateHttpSession(true)
        ;

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