package com.emerchantpay.gateway.genesisandroid.api.models.threedsv2

import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2ControlChallengeIndicator
import com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2.definitions.ThreeDsV2PurchaseCategory

data class ThreeDsV2Params(val controlChallengeIndicator: ThreeDsV2ControlChallengeIndicator? = null,
                           val purchaseCategory: ThreeDsV2PurchaseCategory? = null,
                           val merchantRisk: ThreeDsV2MerchantRiskParams? = null,
                           val cardHolderAccount: ThreeDsV2CardHolderAccountParams? = null,
                           val recurring: ThreeDsV2RecurringParams? = null) {

    private constructor(builder: Builder) : this(builder.controlChallengeIndicator, builder.purchaseCategory,
        builder.merchantRisk, builder.cardHolderAccount, builder.recurring)

    class Builder {
        var controlChallengeIndicator: ThreeDsV2ControlChallengeIndicator? = null
        var purchaseCategory: ThreeDsV2PurchaseCategory? = null
        var merchantRisk: ThreeDsV2MerchantRiskParams? = null
        var cardHolderAccount: ThreeDsV2CardHolderAccountParams? = null
        var recurring: ThreeDsV2RecurringParams? = null

        fun build() = ThreeDsV2Params(this)
    }

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }
}