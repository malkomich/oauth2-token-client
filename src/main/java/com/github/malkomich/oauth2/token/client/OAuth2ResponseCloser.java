package com.github.malkomich.oauth2.token.client;

import com.github.malkomich.oauth2.token.client.exception.OAuth2ClientException;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;

public class OAuth2ResponseCloser implements ResponseCloser {

  @Override
  public void close(CloseableHttpResponse closeableHttpResponse) throws OAuth2ClientException {
    try {
      closeableHttpResponse.close();
    } catch (IOException e) {
      throw new OAuth2ClientException("An error occurred executing the request.", e);
    }
  }
}
