package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/parties/**").hasRole(Role.USER.name())
                .antMatchers(HttpMethod.GET, "/parties").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/members/logout")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);*/
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/parties").permitAll()
                .antMatchers("/login/oauth2/code/google").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/members/logout");

    }
}