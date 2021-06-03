package com.github.malkomich.oauth2.token.client.exception;

public final class ResourceNotFoundClientException extends OAuth2ClientException {

  ResourceNotFoundClientException(int httpCode, String response) {
    super("The OAuth2 URI is not valid or does not exists.", httpCode, response);
  }
}
