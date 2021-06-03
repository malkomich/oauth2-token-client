package com.github.malkomich.oauth2.token.client.exception;

public final class UnsupportedContentType extends OAuth2ClientException {

  public UnsupportedContentType(String contentType) {
    super(String.format("Content type %s is not currently supported.", contentType));
  }
}
