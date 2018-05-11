package com.emerchantpay.gateway.genesisandroid.api.network;

import android.os.AsyncTask;
import android.util.Log;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages;
import com.emerchantpay.gateway.genesisandroid.api.exceptions.UnexpectedException;
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError;
import com.emerchantpay.gateway.genesisandroid.api.util.Configuration;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapper;
import com.emerchantpay.gateway.genesisandroid.api.util.NodeWrapperFactory;
import com.emerchantpay.gateway.genesisandroid.api.util.Request;
import com.emerchantpay.gateway.genesisandroid.api.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class HttpAsyncTask extends AsyncTask<Object, Void, NodeWrapper> implements Serializable {

    enum RequestMethod {
        DELETE, GET, POST, PUT;
    }

    private Configuration configuration;

    public HttpAsyncTask(Configuration configuration) {
        this.configuration = configuration;
    }

    public void delete(String url) {
        httpRequest(RequestMethod.DELETE, url);
    }

    public NodeWrapper get(String url) {
        return httpRequest(RequestMethod.GET, url);
    }

    public NodeWrapper post(String url) {
        return httpRequest(RequestMethod.POST, url, null);
    }

    public NodeWrapper post(String url, Request request) {
        return httpRequest(RequestMethod.POST, url, request.toXML());
    }

    public NodeWrapper postQuery(String url, Request request) {

        return httpRequest(RequestMethod.POST, url, request.toQueryString(""));
    }

    public NodeWrapper put(String url) {
        return httpRequest(RequestMethod.PUT, url, null);
    }

    public NodeWrapper put(String url, Request request) {
        return httpRequest(RequestMethod.PUT, url, request.toXML());
    }

    private NodeWrapper httpRequest(RequestMethod requestMethod, String url) {
        return httpRequest(requestMethod, url, null);
    }

    private NodeWrapper httpRequest(RequestMethod requestMethod, String url, String postBody) {
        HttpURLConnection connection = null;
        NodeWrapper nodeWrapper = null;
        InputStream responseStream = null;
        OutputStream outputStream = null;


        try {
            connection = buildConnection(requestMethod, url);

            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);

            postBody = formatPostBody(postBody);

            outputStream = connection.getOutputStream();
            outputStream.write(postBody.getBytes("UTF-8"));

            responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream()
                    : connection.getInputStream();

            String xml = StringUtils.inputStreamToString(responseStream);

            if (xml != null && configuration.isDebugModeEnabled() == true) {
                Log.d("CURRENT_TIME", getCurrentTime());
                Log.d("REQUEST_METHOD", requestMethod.toString());
                Log.d("URL", url);
                Log.d("RESPONSE_CODE", String.valueOf(connection.getResponseCode()));
                Log.d("POST_BODY", formatSanitizeBodyForLog(postBody));
                Log.d("RESPONSE", formatSanitizeBodyForLog(xml));
            }

            nodeWrapper = NodeWrapperFactory.instance.create(xml);

            throwExceptionIfErrorStatusCode(connection.getResponseCode(), null);
            if (requestMethod.equals(HttpAsyncTask.RequestMethod.DELETE)) {
                return null;
            }
        } catch (IOException e) {
            try {
                throw new UnexpectedException(e.getMessage(), e);
            } catch (UnexpectedException unexpectedException) {
                // TODO Auto-generated catch block
                Log.e("Unexpected Exception", unexpectedException.toString());
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return nodeWrapper;
    }

    private String formatSanitizeBodyForLog(String body) {
        if (body == null) {
            return body;
        }

        Pattern regex = Pattern.compile("(^)", Pattern.MULTILINE);
        Matcher regexMatcher = regex.matcher(body);
        if (regexMatcher.find()) {
            body = regexMatcher.replaceAll("[Genesis] $1");
        }

        regex = Pattern.compile("<number>(.{6}).+?(.{4})</number>");
        regexMatcher = regex.matcher(body);
        if (regexMatcher.find()) {
            body = regexMatcher.replaceAll("<number>$1******$2</number>");
        }

        body = body.replaceAll("<cvv>.+?</cvv>", "<cvv>***</cvv>");

        return body;
    }

    private String formatPostBody(String input) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("d/MMM/yyyy HH:mm:ss Z").format(new Date());
    }

    private HttpURLConnection buildConnection(RequestMethod requestMethod, String urlString)
            throws java.io.IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection;

        String encodedCredentials = configuration.encodeCredentialsToBase64();

        connection = getHttpsURLConnection(url);
        connection.setRequestMethod(requestMethod.toString());
        connection.addRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.addRequestProperty("Authorization", "Basic " + new String(encodedCredentials));
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setReadTimeout(30000);

        return connection;
    }

    public static HttpsURLConnection getHttpsURLConnection(URL url) throws IOException {
        return getHttpsURLConnection(url, false);
    }

    public static HttpsURLConnection getHttpsURLConnection(URL url, boolean compatible) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(url, compatible);
        if (connection instanceof HttpsURLConnection) {
            return (HttpsURLConnection) connection;
        } else {
            throw new IllegalArgumentException("not an HTTPS connection!");
        }
    }

    public static HttpURLConnection getHttpURLConnection(URL url, boolean compatible) throws IOException {
        SSLContext sslcontext;
        try {
            sslcontext = SSLContext.getInstance("TLSv1");
            sslcontext.init((KeyManager[]) null, (TrustManager[]) null, (SecureRandom) null);
        } catch (NoSuchAlgorithmException algorithmException) {
            throw new IllegalArgumentException(algorithmException);
        } catch (KeyManagementException keymanagementException) {
            throw new IllegalArgumentException(keymanagementException);
        }

        SSLSocketFactory socketFactory = new TLSSocketFactory(sslcontext.getSocketFactory(), compatible);
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        return (HttpURLConnection) url.openConnection();
    }

    @Override
    protected NodeWrapper doInBackground(Object... params) {
        return post((String) params[0], (Request) params[1]);
    }

    public static void throwExceptionIfErrorStatusCode(int statusCode, String message) {

        if (isErrorCode(statusCode)) {
            switch (statusCode) {
                case 401:
                    new GenesisError(statusCode, ErrorMessages.AUTHENTICATION_ERROR);
                case 404:
                    new GenesisError(statusCode, ErrorMessages.NOT_FOUND_ERROR);
                case 426:
                    new GenesisError(statusCode, ErrorMessages.UPGRADE_ERROR);
                case 500:
                    new GenesisError(statusCode, ErrorMessages.SERVER_ERROR);
                case 503:
                    new GenesisError(statusCode, ErrorMessages.MAINTENANCE_ERROR);

                default:
                    new GenesisError(statusCode, ErrorMessages.UNEXPECTED_HTTP_ERROR);
            }
        }
    }

    private static boolean isErrorCode(int responseCode) {
        return responseCode != 200 && responseCode != 201 && responseCode != 422;
    }
}