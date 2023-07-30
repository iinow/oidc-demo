package com.example.oidc_demo.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@Slf4j
public class CustomOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        log.info("saveAuthorizedClient`!!!~!");
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        log.info("removeAuthorizedClient`!!!~!");
    }
}
