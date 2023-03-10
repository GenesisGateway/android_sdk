package com.emerchantpay.gateway.genesisandroid.internal

import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringInterval
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringMode
import com.emerchantpay.gateway.genesisandroid.api.interfaces.recurring.CommonManagedRecurringAttributes
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import java.text.SimpleDateFormat
import java.util.*

internal class CommonManagedRecurringAttributesUnitTest : CommonManagedRecurringAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setMode(RecurringMode.AUTOMATIC)
        setInterval(RecurringInterval.DAYS)
        setFirstDate(FIRST_DATE)
        setTimeOfDay(7)
        setPeriod(7)
        setAmount(500)
        setMaxCount(10)

        val generatedXml = buildManagedRecurringAttributes().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    @Test
    @DisplayName("Given required attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerializationWithRequiredParams() {
        setMode(RecurringMode.AUTOMATIC)
        setInterval(RecurringInterval.DAYS)

        val generatedXml = buildManagedRecurringAttributes().toXML()

        assertEquals(EXPECTED_XML_REQUIRED, generatedXml)
    }

    companion object {
        private const val FIRST_DATE_TEXT = "2018-02-14"
        private val FIRST_DATE = SimpleDateFormat("yyyy-MM-dd").parse(FIRST_DATE_TEXT)
        private val MODE = RecurringMode.AUTOMATIC.value
        private val INTERVAL = RecurringInterval.DAYS.value

        private val EXPECTED_XML =
            "<mode>$MODE</mode><interval>$INTERVAL</interval><first_date>$FIRST_DATE_TEXT</first_date><time_of_day>7</time_of_day><period>7</period><amount>500</amount><max_count>10</max_count>"

        private val EXPECTED_XML_REQUIRED =
            "<mode>$MODE</mode><interval>$INTERVAL</interval>"
    }
}