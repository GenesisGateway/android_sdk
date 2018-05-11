package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.network.HttpAsyncTask;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@Config(manifest=Config.NONE)
public class HttpUnitTest {

    private HttpAsyncTask httpHandler;
    private Configuration configuration;
    private NodeWrapper response;

    @Before
    public void setup() {
        configuration = mock(Configuration.class);
        response = mock(NodeWrapper.class);
        httpHandler = new HttpAsyncTask(configuration);
    }

    @Test
    public void testPostRequest() {
        response = httpHandler.post(configuration.getBaseUrl());
        assertEquals(httpHandler.post(configuration.getBaseUrl()), response);
    }

    @Test
    public void testGetRequest() {
        response = httpHandler.get(configuration.getBaseUrl());
        assertEquals(httpHandler.get(configuration.getBaseUrl()), response);
    }

    @Test
    public void testPutRequest() {
        response = httpHandler.put(configuration.getBaseUrl());
        assertEquals(httpHandler.put(configuration.getBaseUrl()), response);
    }

    @Test
    public void testHttpsConnection() throws IOException {
        URL url = new URL("https://google.com");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        assertEquals(httpHandler.getHttpsURLConnection(url, true).getRequestMethod(), connection.getRequestMethod());
    }
}
