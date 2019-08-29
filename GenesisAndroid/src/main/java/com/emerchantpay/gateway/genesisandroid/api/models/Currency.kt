package com.emerchantpay.gateway.genesisandroid.api.models

import java.math.BigDecimal
import java.math.MathContext
import java.util.*
import kotlin.math.pow

class Currency {

    var currency: String = ""
    var exponent: Int? = 0
        private set
    var amount: BigDecimal? = null
        private set

    // Get Currencies
    // Sort Currencies
    val currencies: ArrayList<String>
        @Throws(IllegalAccessException::class)
        get() {
            currencyList.sort()

            return currencyList
        }

    constructor() : super() {
    }

    constructor(currency: String, exponent: Int?) {
        this.currency = currency
        this.exponent = exponent

        currenciesMap[currency] = exponent
        currencyList.add(currency)
    }

    fun getExponent(currency: String): Int? {
        return currenciesMap[currency]
    }

    fun setAmountToExponent(amount: BigDecimal, currency: String): BigDecimal? {

        exponent = getExponent(currency)

        if (exponent!! > 0) {
            val multiplyExp = BigDecimal(10.0.pow(exponent!!.toDouble()), MathContext.DECIMAL64)

            this.amount = amount.multiply(multiplyExp).setScale(0, BigDecimal.ROUND_DOWN)
        } else {
            this.amount = amount.setScale(0, BigDecimal.ROUND_DOWN)
        }

        return this.amount
    }

    fun setExponentToAmount(amount: BigDecimal, currency: String): BigDecimal? {

        exponent = getExponent(currency)

        if (exponent!! > 0) {
            val multiplyExp = BigDecimal(10.0.pow(exponent!!.toDouble()), MathContext.DECIMAL64)

            this.amount = amount.divide(multiplyExp)
        } else {
            this.amount = amount
        }

        return this.amount
    }

    private fun findCurrency(currency: Currency?): Currency? {
        return currency
    }

    @Throws(IllegalAccessException::class)
    fun findCurrencyByName(name: String): Currency? {

        currencyList = currencies

        val exponent = currenciesMap[name]

        for (currencyCode in currencyList) {
            if (currencyCode == name) {
                return Currency(currencyCode, exponent)
            }
        }

        return null
    }

    companion object {

        private var currenciesMap = HashMap<String, Int?>()
        private var currencyList = ArrayList<String>()

        val USD = Currency("USD", 2)
        val EUR = Currency("EUR", 2)
        val CNY = Currency("CNY", 2)
        val GBP = Currency("GBP", 2)
        val KWD = Currency("KWD", 3)
        val JPY = Currency("JPY", 0)
    }
}

