package com.emerchantpay.gateway.genesisandroid.api.interfaces.recurring

import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringInterval
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringMode
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder
import java.text.SimpleDateFormat
import java.util.*

interface CommonManagedRecurringAttributes: IndianManagedRecurringAttributes {

    fun setMode(mode: RecurringMode) {
        requestBuilder.addElement("mode", mode.toString())
    }

    fun setInterval(interval: RecurringInterval) {
        requestBuilder.addElement("interval", interval.toString())
    }

    fun setFirstDate(firstDate: Date?) {
        firstDate?.let { requestBuilder.addElement("first_date", SimpleDateFormat(DATE_FORMAT).format(it)) }
    }

    fun setTimeOfDay(timeOfDay: Int) {
        requestBuilder.addElement("time_of_day", timeOfDay)
    }

    fun setPeriod(period: Int) {
        requestBuilder.addElement("period", period)
    }

    fun setAmount(amount: Int) {
        requestBuilder.addElement("amount", amount)
    }

    fun setMaxCount(maxCount: Int) {
        requestBuilder.addElement("max_count", maxCount)
    }

    fun buildManagedRecurringAttributes(): RequestBuilder = requestBuilder

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private val requestBuilder = RequestBuilder("")
    }
}
