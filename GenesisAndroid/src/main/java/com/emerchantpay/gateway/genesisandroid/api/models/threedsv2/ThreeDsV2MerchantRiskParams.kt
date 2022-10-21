package com.emerchantpay.gateway.genesisandroid.api.models.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.*
import java.util.*

data class ThreeDsV2MerchantRiskParams(val shippingIndicator: ThreeDsV2MerchantRiskShippingIndicator? = null,
                                       val deliveryTimeframe: ThreeDsV2MerchantRiskDeliveryTimeframe? = null,
                                       val reorderItemsIndicator: ThreeDsV2MerchantRiskReorderItemsIndicator? = null,
                                       val preorderPurchaseIndicator: ThreeDsV2MerchantRiskPreorderPurchaseIndicator? = null,
                                       val preorderDate: Date? = null, val isGiftCard: Boolean? = null, val giftCardCount: Int? = null)