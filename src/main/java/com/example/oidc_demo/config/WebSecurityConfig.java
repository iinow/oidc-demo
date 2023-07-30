package com.example.oidc_demo.config;

import com.example.oidc_demo.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(manage -> {
            manage.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        });
        httpSecurity.oauth2Login(oauth -> {
            //리퀘스트 정보를 저장하는 곳을 지정한다. 기본값: 인메모리
//            oauth.authorizedClientService(new CustomOAuth2AuthorizedClientService());

//            oauth.userInfoEndpoint(securityUser -> {
//                securityUser.oidcUserService()
//            });
            oauth.successHandler(new OAuth2SuccessHandler(memberService));
        });
        httpSecurity.authorizeHttpRequests(requests -> {
            requests.requestMatchers(new AntPathRequestMatcher("/login")).permitAll();
            requests.anyRequest().authenticated();
        });
        return httpSecurity.build();
    }
}
