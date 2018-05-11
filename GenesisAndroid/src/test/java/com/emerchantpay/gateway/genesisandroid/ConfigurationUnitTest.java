package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints;
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments;
import com.emerchantpay.gateway.genesisandroid.api.constants.Locales;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;
import com.emerchantpay.gateway.genesisandroid.helpers.Base64;

import org.junit.Test;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

public class ConfigurationUnitTest {

    String username = "Username123";
    String password = "Password123";
    Locale language = Locales.EN;

    Environments environment = new Environments("staging.gate");
    Endpoints endpoint = new Endpoints("emerchantpay.net");

    Configuration configuration = new Configuration(username, password, environment, endpoint, language);

    @Test
    public void testEnvironment() {
        assertEquals(Environments.STAGING.getEnvironmentName(), configuration.getEnvironment().getEnvironmentName());
        assertEquals(Endpoints.EMERCHANTPAY.getEndpointName(), configuration.getEndpoint().getEndpointName());
    }

    @Test
    public void testCredentials() {
        String encodedCredentials = encodeCredentialsToBase64();

        byte[] decodeBytes = Base64.decode(encodedCredentials.getBytes(),
                Base64.NO_WRAP);
        String decodedString = new String(decodeBytes);

        assertEquals(decodedString, username + ":" + password);
    }

    @Test
    public void testChangedCredentials() {
        configuration.setUsername("username");
        configuration.setPassword("password");
        configuration.setToken("test_token");
        configuration.setLanguage(Locales.DE);
        configuration.setWpfEnabled(true);
        configuration.setAction("wpf");

        String encodedCredentials = encodeCredentialsToBase64();

        assertEquals(encodeCredentialsToBase64(), encodedCredentials);
        assertEquals(configuration.getUsername(), "username");
        assertEquals(configuration.getPassword(), "password");
        assertEquals(configuration.getToken(), "test_token");
        assertEquals(configuration.getBaseUrl(),
                "https://staging.gate.emerchantpay.net/wpf/test_token");
        assertTrue(configuration.getWpfEnabled());
    }

    @Test
    public void testEmptyCredentials() {
        configuration.setUsername(null);
        configuration.setPassword(null);
        configuration.setToken(null);

        String encodedCredentials = encodeCredentialsToBase64();

        assertNotSame(encodeCredentialsToBase64(), ":");
        assertEquals(encodeCredentialsToBase64(), encodedCredentials);
    }

    @Test
    public void testDebugMode() {
        // Disabled
        configuration.setDebugMode(false);
        assertFalse(configuration.isDebugModeEnabled());

        // Enabled
        configuration.setDebugMode(true);
        assertTrue(configuration.isDebugModeEnabled());
    }

    @Test
    public void testLanguage() {
        configuration.setLanguage(Locales.EN);

        assertEquals(configuration.getLanguage(), Locales.EN);
    }

    public String encodeCredentialsToBase64() {
        String user_pass = username + ":" + password;
        byte[] encodedBytes = Base64.encode(user_pass.getBytes(),
                Base64.NO_WRAP);
        String encodedString = new String(encodedBytes);

        return encodedString;
    }
}
