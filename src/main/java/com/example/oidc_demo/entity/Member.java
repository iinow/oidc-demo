package com.example.oidc_demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private String uuid;

    private String username;

    private String email;

    public void update(DefaultOidcUser oidcUser) {
        this.email = oidcUser.getEmail();
        this.username = oidcUser.getFullName();
    }
}
