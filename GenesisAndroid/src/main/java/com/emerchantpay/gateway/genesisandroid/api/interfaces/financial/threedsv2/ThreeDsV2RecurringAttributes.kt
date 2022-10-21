package com.emerchantpay.gateway.genesisandroid.api.interfaces.financial.threedsv2

import androidx.annotation.Size
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.text.SimpleDateFormat
import java.util.Date

interface ThreeDsV2RecurringAttributes {
    fun setExpirationDate(expirationDate: Date?) = apply {
        expirationDate?.let { requestBuilder.addElement("expiration_date", SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setFrequency(@Size(min = FREQUENCY_MIN_VALUE, max = FREQUENCY_MAX_VALUE) frequency: Int?) = apply {
        frequency ?: return@apply

        if (frequency in (FREQUENCY_MIN_VALUE..FREQUENCY_MAX_VALUE))
            requestBuilder.addElement("frequency", frequency.toString())
    }

    fun buildRecurringAttributes(): RequestBuilder = requestBuilder

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private const val FREQUENCY_MIN_VALUE = 1L
        private const val FREQUENCY_MAX_VALUE = 9999L
        private val requestBuilder = RequestBuilder("")
    }
}