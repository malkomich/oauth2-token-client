package com.github.malkomich.oauth2.token.client;

import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.CLIENT_CREDENTIALS_GRANT;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.CLIENT_ID;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.CLIENT_SECRET;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.GRANT_TYPE;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.RESOURCE_OWNER_GRANT;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.RESOURCE_OWNER_NAME;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.RESOURCE_OWNER_PASSWORD;
import static org.apache.cxf.rs.security.oauth2.utils.OAuthConstants.SCOPE;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

public final class OAuth2RequestBuilder {

  private final OAuth2Config config;

  OAuth2RequestBuilder(OAuth2Config config) {
    this.config = config;
  }

  HttpUriRequest build() {
    final List<NameValuePair> formData = new ArrayList<>();
    formData.add(new BasicNameValuePair(GRANT_TYPE, config.getGrantType()));

    if (config.getScope() != null && !config.getScope().isBlank()) {
      formData.add(new BasicNameValuePair(SCOPE, config.getScope()));
    }

    if (CLIENT_CREDENTIALS_GRANT.equals(config.getGrantType())) {
      formData.add(new BasicNameValuePair(CLIENT_ID, config.getClientId()));
      formData.add(new BasicNameValuePair(CLIENT_SECRET, config.getClientSecret()));
    }

    if (RESOURCE_OWNER_GRANT.equals(config.getGrantType())) {
      formData.add(new BasicNameValuePair(RESOURCE_OWNER_NAME, config.getUsername()));
      formData.add(new BasicNameValuePair(RESOURCE_OWNER_PASSWORD, config.getPassword()));
    }

    return RequestBuilder.create(HttpPost.METHOD_NAME)
        .setUri(config.getAccessTokenUri())
        .setEntity(new UrlEncodedFormEntity(formData, StandardCharsets.UTF_8))
        .build();
  }
}
