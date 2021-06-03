package com.github.malkomich.oauth2.token.client;

import com.github.malkomich.oauth2.token.client.exception.OAuth2ClientException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpUriRequest;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuth2Client {

  private final OAuth2Config config;
  private final HttpRequestExecutor executor;

  public static OAuth2ClientBuilder withConfig(OAuth2Config config) {
    return new OAuth2ClientBuilder().config(config);
  }

  public AccessToken accessToken() throws OAuth2ClientException {
    final HttpUriRequest request = new OAuth2RequestBuilder(config).build();
    final OAuth2ResponseReader.OAuth2Response response = executor.execute(request).read();

    return AccessToken.withValueMap(OAuth2ResponseHandler.execute(response)).build();
  }

  public static class OAuth2ClientBuilder {
    private OAuth2Config config;
    private HttpRequestExecutor executor;

    private OAuth2ClientBuilder() {}

    private OAuth2ClientBuilder config(OAuth2Config config) {
      this.config = config;
      return this;
    }

    public OAuth2ClientBuilder withExecutor(HttpRequestExecutor executor) {
      this.executor = executor;
      return this;
    }

    public OAuth2Client build() {
      if (executor == null) {
        executor = new HttpRequestExecutor();
      }
      return new OAuth2Client(config, executor);
    }
  }
}
