package com.github.malkomich.oauth2.token.client.exception;

import lombok.Getter;

@Getter
public class OAuth2ClientException extends Exception {

  private int httpCode;
  private String response;

  public OAuth2ClientException(String message) {
    super(message);
  }

  public OAuth2ClientException(String message, int httpCode, String response) {
    super(message);

    this.httpCode = httpCode;
    this.response = response;
  }

  public OAuth2ClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public static OAuth2ClientException fromHttpCode(int code, String response) {

    if (code == 400) {
      return new InvalidRequestClientException(code, response);
    }

    if (code == 401) {
      return new UnauthorizedClientException(code, response);
    }

    if (code == 404) {
      return new ResourceNotFoundClientException(code, response);
    }

    if (code == 500) {
      return new ServerErrorClientException();
    }

    return new OAuth2ClientException("", code, response);
  }
}
