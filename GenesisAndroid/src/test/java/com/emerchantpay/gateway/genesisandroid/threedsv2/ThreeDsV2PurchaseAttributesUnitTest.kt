package com.emerchantpay.gateway.genesisandroid.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.ThreeDsV2PurchaseAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2PurchaseCategory
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName

internal class ThreeDsV2PurchaseAttributesUnitTest : ThreeDsV2PurchaseAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setCategory(CATEGORY)

        val generatedXml = buildPurchaseAttributes().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    companion object {
        private val CATEGORY = ThreeDsV2PurchaseCategory.PREPAID_ACTIVATION
        private val EXPECTED_XML = "<category>$CATEGORY</category>"
    }
}