package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.GooglePayAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.googlepay.definitions.GooglePayPaymentSubtype.*
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.jupiter.api.DisplayName

internal class GooglePayAttributesUnitTest : GooglePayAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setPaymentSubtype(AUTHORIZE)

        val generatedXml = buildGooglePayParams().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    companion object {
        private val EXPECTED_XML =
            "<payment_subtype>authorize</payment_subtype>"
    }
}