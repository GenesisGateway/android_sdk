package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.models.Currency

import org.junit.Test

import java.math.BigDecimal
import java.util.ArrayList

import org.junit.Assert.assertEquals

class CurrencyUnitTest {

    private var amount: BigDecimal? = null
    private var currency: String? = null

    @Test
    fun testTransformAmountToExponent() {

        amount = BigDecimal("10.00")
        currency = Currency.USD.currency

        if (amount != null && currency != null) {

            val curr = Currency()

            curr.setAmountToExponent(amount!!, currency!!)
            amount = curr.amount
        }

        assertEquals(amount, BigDecimal("1000"))
    }

    @Test
    fun testTransformExponentToAmount() {

        amount = BigDecimal("1000.00")
        currency = Currency.USD.currency

        if (amount != null && currency != null) {
            val currency = Currency()

            currency.setExponentToAmount(amount!!, this.currency!!)
            amount = currency.amount
        }

        assertEquals(amount, BigDecimal("10.00"))
    }

    @Test
    fun testGetCurrency() {
        assertEquals(Currency.USD.currency, "USD")
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testFindCurrencyByName() {
        val currency = Currency()

        assertEquals(currency.findCurrencyByName("USD")!!.currency, Currency.USD.currency)
        assertEquals(currency.getExponent("USD"), Currency.USD.exponent)
    }

    @Test
    fun testGetExponent() {
        val currency = Currency()

        assertEquals(currency.getExponent("USD"), Currency.USD.exponent)
    }

    @Test
    @Throws(IllegalAccessException::class)
    fun testGetCurrencies() {
        val currency = Currency()
        val currencyList = currency.currencies

        assertEquals(currency.currencies, currencyList)
    }
}
