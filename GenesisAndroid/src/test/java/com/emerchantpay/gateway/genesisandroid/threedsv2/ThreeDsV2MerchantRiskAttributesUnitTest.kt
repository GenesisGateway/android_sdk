package com.emerchantpay.gateway.genesisandroid.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.ThreeDsV2MerchantRiskAttributes
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import java.text.SimpleDateFormat
import java.util.*

internal class ThreeDsV2MerchantRiskAttributesUnitTest : ThreeDsV2MerchantRiskAttributes {
    @Test
    @DisplayName("Given attributes with assigned values, When serialized to XML, Then should be valid")
    fun testValidXmlIsGeneratedOnSerialization() {
        setShippingIndicator(SHIPPING_INDICATOR)
        setDeliveryTimeframe(DELIVERY_TIMEFRAME)
        setReorderItemsIndicator(REORDER_ITEMS_INDICATOR)
        setPreorderPurchaseIndicator(PREORDER_PURCHASE_INDICATOR)
        setPreorderDate(PREORDER_DATE)
        setIsGiftCard(IS_GIFT_CARD)
        setGiftCardCount(GIFT_CARD_COUNT)

        val generatedXml = buildMerchantRiskAttributes().toXML()

        assertEquals(EXPECTED_XML, generatedXml)
    }

    companion object {
        private val SHIPPING_INDICATOR = ThreeDsV2MerchantRiskShippingIndicator.EVENT_TICKETS
        private val DELIVERY_TIMEFRAME = ThreeDsV2MerchantRiskDeliveryTimeframe.SAME_DAY
        private val REORDER_ITEMS_INDICATOR = ThreeDsV2MerchantRiskReorderItemsIndicator.REORDERED
        private val PREORDER_PURCHASE_INDICATOR = ThreeDsV2MerchantRiskPreorderPurchaseIndicator.MERCHANDISE_AVAILABLE
        private const val PREORDER_DATE_TEXT = "14-02-2018"
        private val PREORDER_DATE = SimpleDateFormat("dd-MM-yyyy").parse(PREORDER_DATE_TEXT)
        private const val IS_GIFT_CARD = true
        private const val GIFT_CARD_COUNT = 3

        private val EXPECTED_XML =
            "<shipping_indicator>$SHIPPING_INDICATOR</shipping_indicator>" +
                    "<delivery_timeframe>$DELIVERY_TIMEFRAME</delivery_timeframe>" +
                    "<reorder_items_indicator>$REORDER_ITEMS_INDICATOR</reorder_items_indicator>" +
                    "<pre_order_purchase_indicator>$PREORDER_PURCHASE_INDICATOR</pre_order_purchase_indicator>" +
                    "<pre_order_date>$PREORDER_DATE_TEXT</pre_order_date>" +
                    "<gift_card>$IS_GIFT_CARD</gift_card>" +
                    "<gift_card_count>$GIFT_CARD_COUNT</gift_card_count>"
    }
}