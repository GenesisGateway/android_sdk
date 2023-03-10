package com.emerchantpay.gateway.genesisandroid.api.interfaces.recurring

import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringAmountType
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringFrequency
import com.emerchantpay.gateway.genesisandroid.api.constants.RecurringPaymentType
import com.emerchantpay.gateway.genesisandroid.api.util.RequestBuilder

interface IndianManagedRecurringAttributes {

    fun setPaymentType(paymentType: RecurringPaymentType) {
        requestBuilder.addElement("payment_type", paymentType.value)
    }

    fun setAmountType(amountType: RecurringAmountType) {
        requestBuilder.addElement("amount_type", amountType.value)
    }

    fun setFrequency(frequency: RecurringFrequency) {
        requestBuilder.addElement("frequency", frequency.value)
    }

    fun setRegistrationReferenceNumber(referenceNumber: Int) {
        requestBuilder.addElement("registration_reference_number", referenceNumber.toString())
    }

    fun setMaxAmount(maxAmount: Int) {
        requestBuilder.addElement("max_amount", maxAmount.toString())
    }

    fun setValidated(validated: Boolean) {
        requestBuilder.addElement("validated", validated.toString())
    }

    fun buildIndianManagedRecurringAttributes(): RequestBuilder = requestBuilder

    companion object {
        private val requestBuilder = RequestBuilder("")
    }
}
