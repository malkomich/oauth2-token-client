package com.github.malkomich.oauth2.token.client;

import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.ACCESS_TOKEN;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.ACCESS_TOKEN_EXPIRES_IN;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.ACCESS_TOKEN_TYPE;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.REFRESH_TOKEN;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccessToken {

  private final long expiresIn;
  private final String tokenType;
  private final String refreshToken;
  private final String accessToken;

  static AccessTokenBuilder withValueMap(JSONObject jsonObject) {
    return new AccessTokenBuilder(jsonObject);
  }

  @RequiredArgsConstructor
  static class AccessTokenBuilder {

    private final JSONObject jsonObject;

    public AccessToken build() {
      return new AccessToken(
          jsonObject.optLong(ACCESS_TOKEN_EXPIRES_IN),
          jsonObject.optString(ACCESS_TOKEN_TYPE),
          jsonObject.optString(REFRESH_TOKEN),
          jsonObject.optString(ACCESS_TOKEN));
    }
  }
}
