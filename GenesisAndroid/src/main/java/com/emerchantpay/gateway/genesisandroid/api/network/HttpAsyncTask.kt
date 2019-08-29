package com.emerchantpay.gateway.genesisandroid.api.network

import android.os.AsyncTask
import android.util.Log
import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorMessages
import com.emerchantpay.gateway.genesisandroid.api.exceptions.UnexpectedException
import com.emerchantpay.gateway.genesisandroid.api.models.GenesisError
import com.emerchantpay.gateway.genesisandroid.api.util.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
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
                    Log.d("POST_BODY", formatSanitizeBodyForLog(postBody))
                    Log.d("RESPONSE", formatSanitizeBodyForLog(xml))
                }
            }

            nodeWrapper = NodeWrapperFactory.instance.create(xml)

            throwExceptionIfErrorStatusCode(connection.responseCode, null)
            when (requestMethod) {
                HttpAsyncTask.RequestMethod.DELETE -> return null
            }
        } catch (e: IOException) {
            try {
                throw UnexpectedException(e.message, e)
            } catch (unexpectedException: UnexpectedException) {
                // TODO Auto-generated catch block
                Log.e("Unexpected Exception", unexpectedException.toString())
            }

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

    @Throws(IOException::class)
    fun getHttpURLConnection(url: URL, compatible: Boolean): HttpURLConnection {
        val sslcontext: SSLContext
        try {
            sslcontext = SSLContext.getInstance("TLSv1")
            sslcontext.init(null as Array<KeyManager>?, null as Array<TrustManager>?, null as SecureRandom?)
        } catch (algorithmException: NoSuchAlgorithmException) {
            throw IllegalArgumentException(algorithmException)
        } catch (keymanagementException: KeyManagementException) {
            throw IllegalArgumentException(keymanagementException)
        }

        val socketFactory = TLSSocketFactory(sslcontext.socketFactory, compatible)
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory)
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