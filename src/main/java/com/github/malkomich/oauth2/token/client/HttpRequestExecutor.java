package com.github.malkomich.oauth2.token.client;

import com.github.malkomich.oauth2.token.client.exception.OAuth2ClientException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public final class HttpRequestExecutor {

  private final CloseableHttpClient httpClient;

  HttpRequestExecutor() {
    this.httpClient = HttpClients.createDefault();
  }

  HttpRequestExecutor(CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public OAuth2ResponseReader execute(HttpUriRequest request) throws OAuth2ClientException {

    final CloseableHttpResponse closeableHttpResponse = doRequest(request);
    final HttpEntity httpEntity = closeableHttpResponse.getEntity();
    final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

    if (statusCode >= 400) {
      final String errorResponse = OAuth2ResponseHandler.extractEntityContent(httpEntity);
      throw OAuth2ClientException.fromHttpCode(statusCode, errorResponse);
    }

    return new OAuth2ResponseReader(closeableHttpResponse);
  }

  private CloseableHttpResponse doRequest(HttpUriRequest request) throws OAuth2ClientException {
    try {
      return httpClient.execute(request);
    } catch (IOException e) {
      throw new OAuth2ClientException("An error occurred executing the request.", e);
    }
  }
}
