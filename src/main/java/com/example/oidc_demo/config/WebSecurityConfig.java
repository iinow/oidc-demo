package com.example.oidc_demo.config;

import com.example.oidc_demo.config.security.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oidc_demo.config.security.JwtFilter;
import com.example.oidc_demo.config.security.JwtProvider;
import com.example.oidc_demo.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(manage -> {
            manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        httpSecurity.oauth2Login(oauth -> {
            //리퀘스트 정보를 저장하는 곳을 지정한다. 기본값: 인메모리
//            oauth.authorizedClientService(new CustomOAuth2AuthorizedClientService());

            oauth.authorizationEndpoint(endpoints -> {
                endpoints.authorizationRequestRepository(new HttpCookieOAuth2AuthorizationRequestRepository());
            });

//            oauth.userInfoEndpoint(securityUser -> {
//                securityUser.oidcUserService()
//            });
            oauth.successHandler(new OAuth2SuccessHandler(memberService, jwtProvider));
//            oauth.failureHandler()
        });
        httpSecurity.authorizeHttpRequests(requests -> {
            requests.requestMatchers(new AntPathRequestMatcher("/login/oauth2/code/local_oidc"),
                    new AntPathRequestMatcher("/login")).permitAll();
            requests.anyRequest().authenticated();
        });

        httpSecurity.addFilterAfter(new JwtFilter(jwtProvider, memberService), OAuth2LoginAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
