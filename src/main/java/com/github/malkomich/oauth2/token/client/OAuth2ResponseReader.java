package com.github.malkomich.oauth2.token.client;

import static org.apache.http.entity.ContentType.DEFAULT_TEXT;

import com.github.malkomich.oauth2.token.client.exception.OAuth2ClientException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.Getter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

public final class OAuth2ResponseReader {

  private final CloseableHttpResponse closeableHttpResponse;
  private final ResponseCloser responseCloser;

  OAuth2ResponseReader(CloseableHttpResponse closeableHttpResponse) {
    this.closeableHttpResponse = closeableHttpResponse;
    this.responseCloser = new OAuth2ResponseCloser();
  }

  public OAuth2Response read() throws OAuth2ClientException {
    final OAuth2Response content = new OAuth2Response(closeableHttpResponse.getEntity());
    responseCloser.close(closeableHttpResponse);

    return content;
  }

  @Getter
  public class OAuth2Response {

    private String content;
    private String contentType;
    private String contentEncoding;
    private long contentLength;

    private OAuth2Response(HttpEntity entity) throws OAuth2ClientException {
      content = OAuth2ResponseHandler.extractEntityContent(entity);
      contentType = getHeaderOrDefault(entity.getContentType(), DEFAULT_TEXT.getMimeType());
      contentEncoding =
          getHeaderOrDefault(entity.getContentEncoding(), StandardCharsets.UTF_8.name());
      contentLength = entity.getContentLength();
    }

    private String getHeaderOrDefault(Header header, String defaultValue) {
      return Optional.ofNullable(header).map(Header::getValue).orElse(defaultValue);
    }
  }
}
