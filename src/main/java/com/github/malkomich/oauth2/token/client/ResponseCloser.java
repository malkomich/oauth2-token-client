package com.github.malkomich.oauth2.token.client;

import com.github.malkomich.oauth2.token.client.exception.OAuth2ClientException;
import org.apache.http.client.methods.CloseableHttpResponse;

public interface ResponseCloser {

  void close(CloseableHttpResponse closeableHttpResponse) throws OAuth2ClientException;
}
