package com.emerchantpay.gateway.genesisandroid.api.network

import android.os.AsyncTask
import android.util.Log
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.util.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateExpiredException
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class HttpAsyncTask(private val configuration: Configuration?) : AsyncTask<Any, Void, NodeWrapper>(), Serializable {

    private val currentTime: String
        get() = SimpleDateFormat("d/MMM/yyyy HH:mm:ss Z").format(Date())

    internal enum class RequestMethod {
        DELETE, GET, POST, PUT
    }

    fun delete(url: String) {
        httpRequest(RequestMethod.DELETE, url)
    }

    operator fun get(url: String?): NodeWrapper? {
        return url?.let { httpRequest(RequestMethod.GET, it) }
    }

    fun post(url: String?): NodeWrapper? {
        return url?.let { httpRequest(RequestMethod.POST, it, null) }
    }

    fun post(url: String, request: Request): NodeWrapper? {
        return httpRequest(RequestMethod.POST, url, request.toXML())
    }

    fun postQuery(url: String, request: Request): NodeWrapper? {

        return httpRequest(RequestMethod.POST, url, request.toQueryString(""))
    }

    fun put(url: String?): NodeWrapper? {
        return url?.let { httpRequest(RequestMethod.PUT, it, null) }
    }

    fun put(url: String, request: Request): NodeWrapper? {
        return httpRequest(RequestMethod.PUT, url, request.toXML())
    }

    private fun httpRequest(requestMethod: RequestMethod, url: String, postBody: String? = null): NodeWrapper? {
        var postBody = postBody
        var connection: HttpURLConnection? = null
        var nodeWrapper: NodeWrapper? = null
        var responseStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            connection = buildConnection(requestMethod, url)

            connection.doOutput = true
            connection.setChunkedStreamingMode(0)

            postBody = formatPostBody(postBody)

            outputStream = connection.outputStream
            outputStream!!.write(postBody.toByteArray(charset("UTF-8")))

            responseStream = if (connection.responseCode == 422)
                connection.errorStream
            else
                connection.inputStream

            val xml = StringUtils.inputStreamToString(responseStream)

            when {
                xml != null && configuration?.isDebugModeEnabled == true -> {
                    Log.d("CURRENT_TIME", currentTime)
                    Log.d("REQUEST_METHOD", requestMethod.toString())
                    Log.d("URL", url)
                    Log.d("RESPONSE_CODE", connection.responseCode.toString())
                    formatSanitizeBodyForLog(postBody)?.let { Log.d("POST_BODY", it) }
                    formatSanitizeBodyForLog(xml)?.let { Log.d("RESPONSE", it) }
                }
            }

            nodeWrapper = NodeWrapperFactory.instance.create(xml)

            throwExceptionIfErrorStatusCode(connection.responseCode, null)

            if (requestMethod == RequestMethod.DELETE) {
                 return null
            }
        } catch (e: Exception) {
            GenesisError(connection?.responseCode, e.message?: ErrorMessages.UNEXPECTED_HTTP_ERROR)
        } finally {
            connection?.disconnect()
        }

        return nodeWrapper
    }

    private fun formatSanitizeBodyForLog(body: String?): String? {
        var body = body
        when (body) {
            null -> return body
            else -> {
                var regex = Pattern.compile("(^)", Pattern.MULTILINE)
                var regexMatcher = regex.matcher(body)
                if (regexMatcher.find()) {
                    body = regexMatcher.replaceAll("[Genesis] $1")
                }

                regex = Pattern.compile("<number>(.{6}).+?(.{4})</number>")
                regexMatcher = regex.matcher(body!!)
                if (regexMatcher.find()) {
                    body = regexMatcher.replaceAll("<number>$1******$2</number>")
                }

                body = body!!.replace("<cvv>.+?</cvv>".toRegex(), "<cvv>***</cvv>")

                return body
            }
        }

    }

    private fun formatPostBody(input: String?): String {
        try {
            val xmlInput = StreamSource(StringReader(input!!))
            val stringWriter = StringWriter()
            val xmlOutput = StreamResult(stringWriter)
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.transform(xmlInput, xmlOutput)
            return xmlOutput.writer.toString()
        } catch (e: Exception) {
            throw RuntimeException(e) // simple exception handling, please review it
        }

    }

    @Throws(java.io.IOException::class)
    private fun buildConnection(requestMethod: RequestMethod, urlString: String): HttpURLConnection {
        val url = URL(urlString)
        val connection: HttpURLConnection

        val encodedCredentials = configuration?.encodeCredentialsToBase64()

        connection = getHttpsURLConnection(url)
        connection.setRequestMethod(requestMethod.toString())
        connection.addRequestProperty("Accept", "application/xml")
        connection.setRequestProperty("Content-Type", "application/xml")
        connection.addRequestProperty("Authorization", "Basic $encodedCredentials")
        connection.setDoInput(true)
        connection.setDoOutput(true)
        connection.setReadTimeout(30000)

        return connection
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Any): NodeWrapper? {
        return post(params[0] as String, params[1] as Request)
    }

    @Throws(IOException::class)
    @JvmOverloads
    fun getHttpsURLConnection(url: URL, compatible: Boolean = false): HttpsURLConnection {
        val connection = getHttpURLConnection(url, compatible)
        return connection as? HttpsURLConnection
                ?: throw IllegalArgumentException("not an HTTPS connection!")
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class)
    fun getHttpURLConnection(url: URL, compatible: Boolean): HttpURLConnection {
        // Create a trust manager that does not validate certificate chains
        val tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        // Initialise the TMF as you normally would, for example:
        tmf.init(null as KeyStore?)

        val trustManagers = tmf.trustManagers.toList()
        val trustManager = trustManagers?.firstOrNull() as? X509TrustManager

        trustManager?: throw NoSuchAlgorithmException("X509 trust manager not supported!")

        val wrappedTrustManagers = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return trustManager.acceptedIssuers
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
                    trustManager.checkClientTrusted(certs, authType)
                }

                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
                    try {
                        trustManager.checkServerTrusted(certs, authType)
                    } catch (e: CertificateExpiredException) {

                    }
                }
            }
        )

        try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, wrappedTrustManagers, null)
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        } catch (algorithmException: NoSuchAlgorithmException) {
            throw IllegalArgumentException(algorithmException)
        } catch (keymanagementException: KeyManagementException) {
            throw IllegalArgumentException(keymanagementException)
        }

        return url.openConnection() as HttpURLConnection
    }

    private fun throwExceptionIfErrorStatusCode(statusCode: Int, message: String?) {

        when {
            isErrorCode(statusCode) -> when (statusCode) {
                401 -> {
                    GenesisError(statusCode, ErrorMessages.AUTHENTICATION_ERROR)
                }
                404 -> {
                    GenesisError(statusCode, ErrorMessages.NOT_FOUND_ERROR)
                }
                426 -> {
                    GenesisError(statusCode, ErrorMessages.UPGRADE_ERROR)
                }
                500 -> {
                    GenesisError(statusCode, ErrorMessages.SERVER_ERROR)
                }
                503 -> {
                    GenesisError(statusCode, ErrorMessages.MAINTENANCE_ERROR)
                }

                else -> GenesisError(statusCode, ErrorMessages.UNEXPECTED_HTTP_ERROR)
            }
        }
    }

    private fun isErrorCode(responseCode: Int): Boolean {
        return responseCode != 200 && responseCode != 201 && responseCode != 422
    }
}