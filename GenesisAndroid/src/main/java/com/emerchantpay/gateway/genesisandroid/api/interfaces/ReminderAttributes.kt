package com.emerchantpay.gateway.genesisandroid.api.interfaces

import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface ReminderAttributes {

    // Risk Params
    fun setChannel(channel: String): ReminderAttributes {
        requestBuilder.addElement("channel", channel)
        return this
    }

    fun setAfter(after: Int?): ReminderAttributes {
        requestBuilder.addElement("after", after!!)
        return this
    }

    fun buildReminders(): RequestBuilder {
        return requestBuilder
    }

    companion object {

        val requestBuilder = RequestBuilder("")
    }
}