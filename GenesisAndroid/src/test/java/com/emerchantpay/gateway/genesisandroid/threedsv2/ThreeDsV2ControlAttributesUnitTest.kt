package com.emerchantpay.gateway.genesisandroid.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.ThreeDsV2ControlAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeIndicator
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeWindowSize
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlDeviceType
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName

internal class ThreeDsV2ControlAttributesUnitTest : ThreeDsV2ControlAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setDeviceType(DEVICE_TYPE)
        setChallengeWindowSize(CHALLENGE_WINDOW_SIZE)
        setChallengeIndicator(CHALLENGE_INDICATOR)

        val generatedXml = buildControlAttributes().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    companion object {
        private val DEVICE_TYPE = ThreeDsV2ControlDeviceType.APPLICATION
        private val CHALLENGE_WINDOW_SIZE = ThreeDsV2ControlChallengeWindowSize.SIZE_500_600
        private val CHALLENGE_INDICATOR = ThreeDsV2ControlChallengeIndicator.MANDATE

        private val EXPECTED_XML =
            "<device_type>$DEVICE_TYPE</device_type>" +
                    "<challenge_window_size>$CHALLENGE_WINDOW_SIZE</challenge_window_size>" +
                    "<challenge_indicator>$CHALLENGE_INDICATOR</challenge_indicator>"
    }
}