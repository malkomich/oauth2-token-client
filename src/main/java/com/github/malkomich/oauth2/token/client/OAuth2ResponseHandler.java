package com.github.malkomich.oauth2.token.client;

import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.github.malkomich.oauth2.token.client.exception.OAuth2ClientException;
import com.github.malkomich.oauth2.token.client.exception.UnsupportedContentType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public final class OAuth2ResponseHandler {

  private OAuth2ResponseHandler() {}

  static JSONObject execute(String content, String contentType) throws OAuth2ClientException {

    if (contentType.contains(APPLICATION_JSON.getMimeType())) {
      return mapJsonResponse(content);
    }

    if (contentType.contains(APPLICATION_FORM_URLENCODED.getMimeType())) {
      return mapURLEncodedResponse(content);
    }

    throw new UnsupportedContentType(contentType);
  }

  static JSONObject execute(OAuth2ResponseReader.OAuth2Response oAuth2Response)
      throws OAuth2ClientException {
    return execute(oAuth2Response.getContent(), oAuth2Response.getContentType());
  }

  static JSONObject execute(HttpEntity httpEntity) throws OAuth2ClientException {
    return execute(extractEntityContent(httpEntity), httpEntity.getContentType().getValue());
  }

  static String extractEntityContent(HttpEntity entity) throws OAuth2ClientException {
    try {
      return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new OAuth2ClientException("An error occurred while extracting entity content.", e);
    }
  }

  private static JSONObject mapJsonResponse(String content) {
    return new JSONObject(content);
  }

  private static JSONObject mapURLEncodedResponse(String content) {
    final JSONObject jsonObject = new JSONObject();

    URLEncodedUtils.parse(content, StandardCharsets.UTF_8)
        .forEach(
            nameValuePair -> jsonObject.put(nameValuePair.getName(), nameValuePair.getValue()));

    return jsonObject;
  }
}
