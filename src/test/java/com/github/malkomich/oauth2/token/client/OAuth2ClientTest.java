package com.github.malkomich.oauth2.token.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.github.malkomich.oauth2.token.client.exception.ResourceNotFoundClientException;
import com.github.malkomich.oauth2.token.client.exception.UnauthorizedClientException;
import com.github.malkomich.oauth2.token.client.validator.GrantValidator;
import com.github.malkomich.oauth2.token.client.validator.GrantValidatorFactory;
import java.io.IOException;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class OAuth2ClientTest {

  private static final String MOCK_OAUTH2_URL = "http://localhost/api/oauth/access_token";

  private OAuth2Client client;

  @Mock private OAuth2Config config;

  @Before
  public void setUp() throws IOException {
    initMocks(this);

    final CloseableHttpClient mockClient = mock(CloseableHttpClient.class);
    client =
        OAuth2Client.withConfig(config).withExecutor(new HttpRequestExecutor(mockClient)).build();

    when(mockClient.execute(any(HttpUriRequest.class)))
        .then(
            (invocation) ->
                handleRequestWithEntity(
                    invocation.getArgumentAt(0, HttpEntityEnclosingRequestBase.class)));
  }

  @Test
  public void testGetAccessTokenGrantPassword() throws Exception {
    when(config.getGrantType()).thenReturn(OAuthConstants.RESOURCE_OWNER_GRANT);
    when(config.getAccessTokenUri()).thenReturn(MOCK_OAUTH2_URL);
    when(config.getUsername()).thenReturn(GrantValidator.USERNAME);
    when(config.getPassword()).thenReturn(GrantValidator.PASSWORD);

    final AccessToken token = client.accessToken();

    assertNotNull(token);
    assertEquals(
        GrantValidator.ACCESS_TOKEN_RESPONSE.get(OAuthConstants.ACCESS_TOKEN),
        token.getAccessToken());
    assertEquals(
        GrantValidator.ACCESS_TOKEN_RESPONSE.get(OAuthConstants.ACCESS_TOKEN_TYPE),
        token.getTokenType());
  }

  @Test(expected = UnauthorizedClientException.class)
  public void testGetAccessTokenGrantPasswordWrongUserCredentials() throws Exception {
    when(config.getGrantType()).thenReturn(OAuthConstants.RESOURCE_OWNER_GRANT);
    when(config.getAccessTokenUri()).thenReturn(MOCK_OAUTH2_URL);
    when(config.getUsername()).thenReturn("userWrong");
    when(config.getPassword()).thenReturn(GrantValidator.PASSWORD);

    client.accessToken();
  }

  @Test
  public void testGetAccessTokenGrantClientCredentials() throws Exception {
    when(config.getGrantType()).thenReturn(OAuthConstants.CLIENT_CREDENTIALS_GRANT);
    when(config.getAccessTokenUri()).thenReturn(MOCK_OAUTH2_URL);
    when(config.getClientId()).thenReturn(GrantValidator.CLIENT_ID);
    when(config.getClientSecret()).thenReturn(GrantValidator.CLIENT_SECRET);

    final AccessToken token = client.accessToken();

    assertNotNull(token);
    assertEquals(
        GrantValidator.ACCESS_TOKEN_RESPONSE.get(OAuthConstants.ACCESS_TOKEN),
        token.getAccessToken());
    assertEquals(
        GrantValidator.ACCESS_TOKEN_RESPONSE.get(OAuthConstants.ACCESS_TOKEN_TYPE),
        token.getTokenType());
  }

  @Test(expected = UnauthorizedClientException.class)
  public void testGetAccessTokenGrantClientCredentialsWrongCredentials() throws Exception {
    when(config.getGrantType()).thenReturn(OAuthConstants.CLIENT_CREDENTIALS_GRANT);
    when(config.getAccessTokenUri()).thenReturn(MOCK_OAUTH2_URL);
    when(config.getClientId()).thenReturn("clientIdWrong");
    when(config.getClientSecret()).thenReturn(GrantValidator.CLIENT_SECRET);

    client.accessToken();
  }

  @Test(expected = ResourceNotFoundClientException.class)
  public void testGetAccessTokenGrantClientCredentialsWrongUrl() throws Exception {
    when(config.getGrantType()).thenReturn(OAuthConstants.CLIENT_CREDENTIALS_GRANT);
    when(config.getAccessTokenUri()).thenReturn("accessTokenUriWrong");
    when(config.getClientId()).thenReturn("clientIdWrong");
    when(config.getClientSecret()).thenReturn(GrantValidator.CLIENT_SECRET);

    client.accessToken();
  }

  private CloseableHttpResponse handleRequestWithEntity(HttpEntityEnclosingRequestBase request)
      throws Exception {

    final CloseableHttpResponse response = mock(CloseableHttpResponse.class);

    if (!MOCK_OAUTH2_URL.equals(request.getURI().toString())
        || !HttpPost.METHOD_NAME.equals(request.getMethod())) {
      when(response.getEntity()).thenReturn(new StringEntity("Not Found"));
      when(response.getStatusLine()).thenReturn(createStatusLine(404));
      return response;
    }

    final JSONObject jsonObject = OAuth2ResponseHandler.execute(request.getEntity());

    final GrantValidator grantValidator =
        GrantValidatorFactory.fromGrantType(
            String.valueOf(jsonObject.get(OAuthConstants.GRANT_TYPE)));

    if (!grantValidator.validateGrant(jsonObject)) {
      when(response.getStatusLine()).thenReturn(createStatusLine(grantValidator.errorStatusCode()));
      when(response.getEntity()).thenReturn(grantValidator.unauthorizedEntity());
      return response;
    }

    when(response.getEntity()).thenReturn(grantValidator.authorizedEntity());
    when(response.getStatusLine()).thenReturn(createStatusLine(200));

    return response;
  }

  private StatusLine createStatusLine(int code) {
    return new BasicStatusLine(HttpVersion.HTTP_1_1, code, null);
  }
}
