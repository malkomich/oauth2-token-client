package com.github.malkomich.oauth2.token.client.validator;

import java.util.Map;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.json.JSONObject;

public class ClientCredentialsGrantValidator extends GrantValidator {

  ClientCredentialsGrantValidator() {}

  @Override
  public boolean validateGrant(JSONObject jsonObject) {
    return jsonObject.get(OAuthConstants.CLIENT_ID).equals(CLIENT_ID)
        && jsonObject.get(OAuthConstants.CLIENT_SECRET).equals(CLIENT_SECRET);
  }

  @Override
  protected Map<String, Object> unauthorizedData() {
    return Map.of(
        "error", "invalid_client",
        "error_description", "Client authentication failed.");
  }

  @Override
  protected Map<String, Object> authorizedData() {
    return Map.of("access_token", ACCESS_TOKEN, "token_type", "Bearer", "expires_in", 3600);
  }
}
