package com.github.malkomich.oauth2.token.client.exception;

public final class UnauthorizedClientException extends OAuth2ClientException {

  UnauthorizedClientException(int httpCode, String response) {
    super(
        "The client is not authorized to request an access token using this method.",
        httpCode,
        response);
  }
}
