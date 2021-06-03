package com.github.malkomich.oauth2.token.client.validator;

import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public abstract class GrantValidator {

  public static String CLIENT_ID = "mockedClientId";
  public static String CLIENT_SECRET = "mockedClientSecret";
  public static String USERNAME = "mockedUser";
  public static String PASSWORD = "mockedPassword";
  public static String REFRESH_TOKEN = "mockedRefreshToken";
  public static String ACCESS_TOKEN = "mockedAccessToken";
  public static Map<String, Object> ACCESS_TOKEN_RESPONSE =
      Map.of(
          "access_token",
          ACCESS_TOKEN,
          "token_type",
          "Bearer",
          "expires_in",
          3600,
          "refresh_token",
          REFRESH_TOKEN);

  public HttpEntity unauthorizedEntity() {
    return createHttpEntity(unauthorizedData());
  }

  public HttpEntity authorizedEntity() {
    return createHttpEntity(authorizedData());
  }

  public int errorStatusCode() {
    return HttpStatus.SC_UNAUTHORIZED;
  }

  public abstract boolean validateGrant(JSONObject jsonObject);

  protected abstract Map<String, Object> unauthorizedData();

  protected abstract Map<String, Object> authorizedData();

  private HttpEntity createHttpEntity(Map map) {
    final String json = new JSONObject(map).toString();

    return new StringEntity(json, ContentType.APPLICATION_JSON);
  }
}
