package com.github.malkomich.oauth2.token.client.validator;

import java.util.Map;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.json.JSONObject;

public class RefreshTokenGrantValidator extends GrantValidator {

  RefreshTokenGrantValidator() {}

  @Override
  public boolean validateGrant(JSONObject jsonObject) {
    return jsonObject.has(OAuthConstants.REFRESH_TOKEN)
        && jsonObject.get(OAuthConstants.REFRESH_TOKEN).equals(REFRESH_TOKEN);
  }

  @Override
  protected Map<String, Object> unauthorizedData() {
    return Map.of(
        "error", "invalid_request",
        "error_description", "The refresh token is invalid.");
  }

  @Override
  protected Map<String, Object> authorizedData() {
    return Map.of(
        "access_token",
        ACCESS_TOKEN,
        "token_type",
        "Bearer",
        "expires_in",
        3600,
        "refresh_token",
        REFRESH_TOKEN);
  }
}
