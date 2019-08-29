package com.emerchantpay.gateway.genesisandroid

import android.util.Base64
import com.emerchantpay.gateway.genesisandroid.api.constants.Endpoints
import com.emerchantpay.gateway.genesisandroid.api.constants.Environments
import com.emerchantpay.gateway.genesisandroid.api.constants.Locales
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConfigurationUnitTest {

    internal var username = "Username123"
    internal var password = "Password123"
    internal var language = Locales.EN

    internal var environment = Environments("staging.gate")
    internal var endpoint = Endpoints("emerchantpay.net")

    internal var configuration = Configuration(username, password, environment, endpoint, language)

    @Test
    fun testEnvironment() {
        assertEquals(Environments.STAGING.environmentName, configuration.environment.environmentName)
        assertEquals(Endpoints.EMERCHANTPAY.endpointName, configuration.endpoint.endpointName)
    }

    @Test
    fun testCredentials() {
        val encodedCredentials = encodeCredentialsToBase64()

        val decodeBytes = Base64.decode(encodedCredentials.toByteArray(),
                Base64.NO_WRAP)
        val decodedString = String(decodeBytes)

        assertEquals(decodedString, "$username:$password")
    }

    @Test
    fun testChangedCredentials() {
        configuration.username = "username"
        configuration.password = "password"
        configuration.token = "test_token"
        configuration.language = Locales.DE
        configuration.wpfEnabled = true
        configuration.action = "wpf"

        val encodedCredentials = encodeCredentialsToBase64()

        assertEquals(encodeCredentialsToBase64(), encodedCredentials)
        assertEquals(configuration.username, "username")
        assertEquals(configuration.password, "password")
        assertEquals(configuration.token, "test_token")
        assertEquals(configuration.baseUrl,
                "https://staging.gate.emerchantpay.net/wpf/test_token")
        assertTrue(configuration.wpfEnabled!!)
    }

    @Test
    fun testEmptyCredentials() {
        configuration.username = null
        configuration.password = null
        configuration.token = null

        val encodedCredentials = encodeCredentialsToBase64()

        assertNotSame(encodeCredentialsToBase64(), ":")
        assertEquals(encodeCredentialsToBase64(), encodedCredentials)
    }

    @Test
    fun testDebugMode() {
        // Disabled
        configuration.setDebugMode(false)
        assertFalse(configuration.isDebugModeEnabled!!)

        // Enabled
        configuration.setDebugMode(true)
        assertTrue(configuration.isDebugModeEnabled!!)
    }

    @Test
    fun testLanguage() {
        configuration.language = Locales.EN

        assertEquals(configuration.language, Locales.EN)
    }

    fun encodeCredentialsToBase64(): String {
        val user_pass = "$username:$password"
        val encodedBytes = Base64.encode(user_pass.toByteArray(),
                Base64.NO_WRAP)

        return String(encodedBytes)
    }
}
