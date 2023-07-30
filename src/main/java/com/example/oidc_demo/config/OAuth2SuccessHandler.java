package com.example.oidc_demo.config;

import com.example.oidc_demo.common.CookieUtils;
import com.example.oidc_demo.config.security.JwtFilter;
import com.example.oidc_demo.config.security.JwtProvider;
import com.example.oidc_demo.entity.Member;
import com.example.oidc_demo.member.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    /**
     * TODO OIDC 연동시 Authentication.principal 값은 DefaultOidcUser 값으로 오고 있다. 이것을 래핑해야한다.
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("authentication: {}", authentication.getPrincipal());
        Member member = null;
        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            Optional<Member> opMember = memberService.findMemberByEmail(oidcUser.getEmail());
            if (opMember.isPresent()) {
                member = updateMember(oidcUser, opMember.get());
            } else {
                member = registerMember(oidcUser);
            }
        }
        assert member != null;
        String jwt = jwtProvider.createJwt(member.getUuid());
        CookieUtils.addCookie(response, JwtFilter.JWT_COOKIE_NAME, jwt, jwtProvider.getExpiredTime());
        response.sendRedirect("/");
    }

    /**
     * 최초 회원가입
     * @param oidcUser
     */
    private Member registerMember(DefaultOidcUser oidcUser) {
        Member member = Member.builder()
                .email(oidcUser.getEmail())
                .username(oidcUser.getFullName())
                .build();
        return memberService.createMember(member);
    }

    /**
     * 기존 회원가입 사용자
     * @param oidcUser
     * @param member
     */
    private Member updateMember(DefaultOidcUser oidcUser, Member member) {
        member.update(oidcUser);
        return memberService.createMember(member);
    }
}
