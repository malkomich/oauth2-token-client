package com.github.malkomich.oauth2.token.client.exception;

public final class InvalidRequestClientException extends OAuth2ClientException {

  InvalidRequestClientException(int httpCode, String response) {
    super("The request is not valid or contains malformed parameters.", httpCode, response);
  }
}
