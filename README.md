# OIDC Spring security

* `Authentication` <- `OAuth2AuthenticationToken`
  * `OAuth2AuthenticationToken.principal`: `DefaultOidcUser`
* `OAuth2AuthorizedClientService` 이쪽을 구현해야할 것 같다.