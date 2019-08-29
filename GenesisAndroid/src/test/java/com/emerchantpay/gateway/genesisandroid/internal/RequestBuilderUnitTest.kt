package com.emerchantpay.gateway.genesisandroid.internal

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilderWithAttribute
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class RequestBuilderUnitTest {
    @Test
    fun toXmlBuilder() {
        val builder = RequestBuilder("payment_transaction")
        builder.addElement("name", "value")
        val result = builder.toXML()
        assertEquals("<payment_transaction><name>value</name></payment_transaction>", result)
    }

    // reveal protected methods for test
    private class Open : RequestBuilder("open") {
        companion object {

            fun publicBuildXmlElement(name: String, `object`: Any): String? {
                return RequestBuilder.buildXMLElement(name, `object`)
            }

            fun formatMap(name: String, map: Map<String, Any>): String {
                return RequestBuilder.formatAsXML(name, map)
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    fun listBuilder() {
        @SuppressWarnings("unused")
        val builder = Open()
        @SuppressWarnings("rawtypes")
        val items = arrayListOf<String>()
        items.add("Authorize")
        items.add("WPF")
        val element = Open.publicBuildXmlElement("payments", items)
        assertEquals("<payments type=\"array\"><item>Authorize</item><item>WPF</item></payments>", element)
    }


    @Test
    fun mapBuilder() {
        @SuppressWarnings("unused")
        val builder = Open()
        val map = HashMap<String, Any>()
        map["transaction_id"] = "123456"
        map["transaction_type"] = "Authorize"
        val element = Open.formatMap("payments", map)
        assertEquals(
                "<payments><transaction_id>123456</transaction_id><transaction_type>Authorize</transaction_type></payments>",
                element)
    }

    @Test
    fun toXmlBilderWithAttribute() {
        val builder = RequestBuilderWithAttribute("payments", "Authorize")
        builder.addElement("name", "value")
        val result = builder.toXML()
        assertEquals("<payments name=\"Authorize\"><name>value</name></payments>", result)
    }

    // reveal protected methods for test
    private class OpenBuilderWithAttribute : RequestBuilder("open") {
        companion object {

            fun publicBuildXmlElement(name: String, `object`: Any): String? {
                return RequestBuilder.buildXMLElement(name, `object`)
            }

            fun formatMap(name: String, map: Map<String, Any>): String {
                return RequestBuilder.formatAsXML(name, map)
            }
        }
    }

    @Test
    fun listBuilderWithAttribute() {
        @SuppressWarnings("unused")
        val builder = Open()
        val items = arrayListOf<String>()
        items.add("Authorize")
        items.add("WPF")
        val element = Open.publicBuildXmlElement("payments", items)
        assertEquals("<payments type=\"array\"><item>Authorize</item><item>WPF</item></payments>", element)
    }

    @Test
    fun mapBuilderWithAttribute() {
        @SuppressWarnings("unused")
        val builder = Open()
        val map = HashMap<String, Any>()
        map["transaction_id"] = "123456"
        map["transaction_type"] = "Authorize"
        val element = Open.formatMap("payments", map)
        assertEquals(
                "<payments><transaction_id>123456</transaction_id><transaction_type>Authorize</transaction_type></payments>",
                element)
    }
}
