package com.emerchantpay.gateway.genesisandroid.api.models.threedsv2

import androidx.annotation.Size
import java.util.*

data class ThreeDsV2RecurringParams(val expirationDate: Date? = null,
                                    @Size(min = FREQUENCY_MIN_VALUE, max = FREQUENCY_MAX_VALUE) val frequency: Int? = null) {
    companion object {
        private const val FREQUENCY_MIN_VALUE = 1L
        private const val FREQUENCY_MAX_VALUE = 9999L
    }
}