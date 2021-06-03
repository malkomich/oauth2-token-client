package com.github.malkomich.oauth2.token.client.validator;

import java.util.Map;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.json.JSONObject;

public class PasswordGrantValidator extends GrantValidator {

  PasswordGrantValidator() {}

  @Override
  public boolean validateGrant(JSONObject jsonObject) {
    return jsonObject.get(OAuthConstants.RESOURCE_OWNER_NAME).equals(USERNAME)
        && jsonObject.get(OAuthConstants.RESOURCE_OWNER_PASSWORD).equals(PASSWORD);
  }

  @Override
  protected Map<String, Object> unauthorizedData() {
    return Map.of(
        "error", "invalid_credentials",
        "error_description", "The user credentials were incorrect.");
  }

  @Override
  protected Map<String, Object> authorizedData() {
    return Map.of("access_token", ACCESS_TOKEN, "token_type", "Bearer", "expires_in", 3600);
  }
}
