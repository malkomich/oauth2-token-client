package com.github.malkomich.oauth2.token.client.exception;

public final class ServerErrorClientException extends OAuth2ClientException {

  ServerErrorClientException() {
    super("An error occurred on the authorization server.");
  }
}
