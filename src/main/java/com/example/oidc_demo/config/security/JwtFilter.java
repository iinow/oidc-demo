package com.example.oidc_demo.config.security;

import com.example.oidc_demo.common.CookieUtils;
import com.example.oidc_demo.entity.Member;
import com.example.oidc_demo.member.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String JWT_COOKIE_NAME = "HAHAH";

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        if (uri.startsWith("/login")) {
            return true;
        }

        if (uri.startsWith("/logout")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = CookieUtils.getCookie(request, JWT_COOKIE_NAME)
                .map(Cookie::getValue)
                .filter(v -> !ObjectUtils.isEmpty(v))
                .orElse(null);
        if (jwt != null) {
            String uuid = jwtProvider.parseJwt(jwt);
            Member member = memberService.findMember(uuid);
            SecurityMember securityMember = SecurityMember.builder()
                    .member(member)
                    .build();

            SecurityContextHolder.getContext().setAuthentication(new OAuth2AuthenticationToken(securityMember, null, "local_oidc"));
        }
        filterChain.doFilter(request, response);
    }
}
