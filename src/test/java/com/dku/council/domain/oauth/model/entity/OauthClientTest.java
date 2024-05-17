package com.dku.council.domain.oauth.model.entity;

import com.dku.council.domain.oauth.exception.InvalidClientSecretException;
import com.dku.council.domain.oauth.exception.InvalidOauthClientIdException;
import com.dku.council.domain.oauth.exception.InvalidOauthRedirectUriException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OauthClientTest {
    private static String clientId;
    private static String applicationName;
    private static String clientSecret;
    private static String redirectUri;
    private static String scope;
    @BeforeAll
    static void setUp() {
        clientId = "clientId";
        applicationName = "appName";
        clientSecret = "clientSecret";
        redirectUri = "https://redirecturi.com";
        scope = "studentId gender";
    }

    @Test
    void createOauthClientWithGivenParameters() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertEquals(clientId, oauthClient.getClientId());
        assertEquals(applicationName, oauthClient.getApplicationName());
        assertEquals(clientSecret, oauthClient.getClientSecret());
        assertEquals(redirectUri, oauthClient.getRedirectUri());
    }

    @Test
    void throwExceptionWhenClientIdDoesNotMatch() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertThrows(InvalidOauthClientIdException.class, () -> oauthClient.checkClientId("invalidId"),
                "invalid.oauth-client-id: invalidId");
    }

    @Test
    void doesNotThrowExceptionWhenClientIdMatches() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertDoesNotThrow(() -> oauthClient.checkClientId(clientId));
    }

    @Test
    void throwExceptionWhenRedirectUriDoesNotMatch() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertThrows(InvalidOauthRedirectUriException.class, () -> oauthClient.checkRedirectUri("invalidUri"),
                "invalid.oauth-redirect-uri: invalidUri");
    }

    @Test
    void doesNotThrowExceptionWhenRedirectUriMatches() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertDoesNotThrow(() -> oauthClient.checkRedirectUri(redirectUri));
    }

    @Test
    void throwExceptionWhenClientSecretDoesNotMatch() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertThrows(InvalidClientSecretException.class, () -> oauthClient.checkClientSecret("invalidSecret"),
                "invalid.client-secret: invalidSecret");
    }

    @Test
    void doesNotThrowExceptionWhenClientSecretMatches() {
        OauthClient oauthClient = OauthClient.of(clientId, applicationName, clientSecret, redirectUri, scope);
        assertDoesNotThrow(() -> oauthClient.checkClientSecret(clientSecret));
    }
}
