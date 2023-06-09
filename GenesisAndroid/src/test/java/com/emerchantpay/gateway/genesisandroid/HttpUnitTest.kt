package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.network.HttpAsyncTask
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper

import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

import java.io.IOException
import java.net.URL

import javax.net.ssl.HttpsURLConnection

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.mock
import java.security.NoSuchAlgorithmException

@Config(manifest = Config.NONE)
class HttpUnitTest {

    private var httpHandler: HttpAsyncTask? = null
    private var configuration: Configuration? = null
    private var response: NodeWrapper? = null

    @Before
    fun setup() {
        configuration = mock(Configuration::class.java)
        response = mock(NodeWrapper::class.java)
        httpHandler = HttpAsyncTask(configuration!!)
    }

    @Test
    fun testPostRequest() {
        response = httpHandler!!.post(configuration!!.baseUrl)
        assertEquals(httpHandler!!.post(configuration!!.baseUrl), response)
    }

    @Test
    fun testGetRequest() {
        response = httpHandler!![configuration!!.baseUrl]
        assertEquals(httpHandler!![configuration!!.baseUrl], response)
    }

    @Test
    fun testPutRequest() {
        response = httpHandler!!.put(configuration!!.baseUrl)
        assertEquals(httpHandler!!.put(configuration!!.baseUrl), response)
    }

    @Test
    @Throws(IOException::class, NoSuchAlgorithmException::class)
    fun testHttpsConnection() {
        val url = URL("https://google.com")
        val connection = url.openConnection() as HttpsURLConnection

        assertEquals(httpHandler!!.getHttpsURLConnection(url, true).requestMethod, connection.requestMethod)
    }
}
