package com.emerchantpay.gateway.genesisandroid.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.ThreeDsV2RecurringAttributes
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import java.text.SimpleDateFormat
import java.util.*

internal class ThreeDsV2RecurringAttributesUnitTest : ThreeDsV2RecurringAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setExpirationDate(EXPIRATION_DATE)
        setFrequency(FREQUENCY)

        val generatedXml = buildRecurringAttributes().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    companion object {
        private const val EXPIRATION_DATE_TEXT = "14-02-2018"
        private const val FREQUENCY = 30
        private val EXPIRATION_DATE = SimpleDateFormat("dd-MM-yyyy").parse(EXPIRATION_DATE_TEXT)

        private const val EXPECTED_XML =
            "<expiration_date>$EXPIRATION_DATE_TEXT</expiration_date><frequency>$FREQUENCY</frequency>"
    }
}