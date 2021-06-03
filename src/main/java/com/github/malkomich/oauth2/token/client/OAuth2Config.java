package com.github.malkomich.oauth2.token.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Config {
  private String grantType;
  private String clientId;
  private String clientSecret;
  private String username;
  private String password;
  private String accessTokenUri;
  private String scope;
}
