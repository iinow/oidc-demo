package com.example.oidc_demo.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class JwtProvider {

    private String USER_KEY = "USER_KEY";

    @Value("${jwt.secret:TEST_SECRET}")
    private String jwtSecret;

    @Value("${jwt.issuer:DEMO}")
    private String issuer;

    @Value("${jwt.expire-hours:24}")
    private int expireHours = 0;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

    private JWTVerifier jwtVerifier() {
        return JWT.require(algorithm())
                .withIssuer(issuer)
                .build();
    }

    public String createJwt(String userSeq) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiredTime = now.plusHours(expireHours);
        return JWT.create()
                .withIssuer(issuer)
                .withClaim(USER_KEY, userSeq)
                .withIssuedAt(now.toInstant())
                .withExpiresAt(expiredTime.toInstant())
                .sign(algorithm());
    }

    public int getExpiredTime() {
        return expireHours * 60 * 60;
    }

    public String parseJwt(String jwt) {
        DecodedJWT decodedJWT = jwtVerifier().verify(jwt);
        return decodedJWT.getClaim(USER_KEY).asString();
    }
}
