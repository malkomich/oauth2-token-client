package com.github.malkomich.oauth2.token.client.validator;

import java.util.Map;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class UnsupportedGrantValidator extends GrantValidator {

  UnsupportedGrantValidator() {}

  @Override
  public int errorStatusCode() {
    return HttpStatus.SC_BAD_REQUEST;
  }

  @Override
  public boolean validateGrant(JSONObject jsonObject) {
    return false;
  }

  @Override
  protected Map<String, Object> unauthorizedData() {
    return Map.of(
        "error", "unsupported_grant_type",
        "error_description",
            "The authorization grant type \"passwords\" is not supported by the authorization server.");
  }

  @Override
  protected Map<String, Object> authorizedData() {
    throw new UnsupportedOperationException();
  }
}
