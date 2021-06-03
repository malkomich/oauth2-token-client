package com.github.malkomich.oauth2.token.client.validator;

import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.CLIENT_CREDENTIALS_GRANT;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.REFRESH_TOKEN_GRANT;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.RESOURCE_OWNER_GRANT;

public class GrantValidatorFactory {

  public static GrantValidator fromGrantType(String grantType) {

    if (CLIENT_CREDENTIALS_GRANT.equals(grantType)) {
      return new ClientCredentialsGrantValidator();
    }

    if (RESOURCE_OWNER_GRANT.equals(grantType)) {
      return new PasswordGrantValidator();
    }

    if (REFRESH_TOKEN_GRANT.equals(grantType)) {
      return new RefreshTokenGrantValidator();
    }

    return new UnsupportedGrantValidator();
  }
}
