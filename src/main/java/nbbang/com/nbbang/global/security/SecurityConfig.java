package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.filter.SimpleRequestHeaderLoggingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final LogoutService logoutService;

    @Value("${request.logging:false}")
    private Boolean requestLogging;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(logoutService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (requestLogging) http.addFilterBefore(new SimpleRequestHeaderLoggingFilter(), WebAsyncManagerIntegrationFilter.class);
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(utf8EncodingFilter(), WebAsyncManagerIntegrationFilter.class)
                //.addFilterBefore(new CustomRequestLoggingFilter(), WebAsyncManagerIntegrationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new LoginRedirectionFilter(), WebAsyncManagerIntegrationFilter.class)
                .csrf().disable()
                .cors()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers(HttpMethod.GET, "/parties").permitAll()
                .antMatchers(HttpMethod.GET, "/parties/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2MemberService)
                .and()
                .successHandler(new OAuth2AuthenticationSuccessHandler());
    }



    private AuthenticationEntryPoint getRestAuthenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.BAD_GATEWAY);
    }


    private CharacterEncodingFilter utf8EncodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return encodingFilter;
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