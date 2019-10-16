package com.emerchantpay.gateway.genesisandroid.api.models

import java.math.BigDecimal
import java.math.MathContext
import java.util.*
import kotlin.math.pow

class Currency {

    var currency: String = ""
    var exponent: Int? = 2
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
        exponent = if (currenciesMap[currency] != null) {
            getExponent(currency)
        } else {
            2
        }

        if (exponent!! > 0) {
            val multiplyExp = BigDecimal(10.0.pow(exponent!!.toDouble()), MathContext.DECIMAL64)

            this.amount = amount.multiply(multiplyExp).setScale(0, BigDecimal.ROUND_DOWN)
        } else {
            this.amount = amount.setScale(0, BigDecimal.ROUND_DOWN)
        }

        return this.amount
    }

    fun setExponentToAmount(amount: BigDecimal, currency: String): BigDecimal? {
        exponent = if (currenciesMap[currency] != null) {
            getExponent(currency)
        } else {
            2
        }

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
        val AED = Currency("AED", 2)
        val AFN = Currency("AFN", 2)
        val ALL = Currency("ALL", 2)
        val AMD = Currency("AMD", 2)
        val ANG = Currency("ANG", 2)
        val AOA = Currency("AOA", 2)
        val ARS = Currency("ARS", 2)
        val AUD = Currency("AUD", 2)
        val AWG = Currency("AWG", 2)
        val AZN = Currency("AZN", 2)
        val BAM = Currency("BAM", 2)
        val BBD = Currency("BBD", 2)
        val BDT = Currency("BDT", 2)
        val BGN = Currency("BGN", 2)
        val BHD = Currency("BHD", 3)
        val BIF = Currency("BIF", 0)
        val BMD = Currency("BMD", 2)
        val BND = Currency("BND", 2)
        val BOB = Currency("BOB", 2)
        val BOV = Currency("BOV", 2)
        val BRL = Currency("BRL", 2)
        val BSD = Currency("BSD", 2)
        val BTN = Currency("BTN", 2)
        val BWP = Currency("BWP", 2)
        val BYR = Currency("BYR", 0)
        val BZD = Currency("BZD", 2)
        val CAD = Currency("CAD", 2)
        val CDF = Currency("CDF", 2)
        val CHE = Currency("CHE", 2)
        val CHF = Currency("CHF", 2)
        val CHW = Currency("CHW", 2)
        val CLF = Currency("CLF", 4)
        val CLP = Currency("CLP", 0)
        val COP = Currency("COP", 2)
        val COU = Currency("COU", 2)
        val CRC = Currency("CRC", 2)
        val CUC = Currency("CUC", 2)
        val CUP = Currency("CUP", 2)
        val CVE = Currency("CVE", 2)
        val CZK = Currency("CZK", 2)
        val DJF = Currency("DJF", 0)
        val DKK = Currency("DKK", 2)
        val DOP = Currency("DOP", 2)
        val DZD = Currency("DZD", 2)
        val EGP = Currency("EGP", 2)
        val ERN = Currency("ERN", 2)
        val ETB = Currency("ETB", 2)
        val FJD = Currency("FJD", 2)
        val FKP = Currency("FKP", 2)
        val GEL = Currency("GEL", 2)
        val GHS = Currency("GHS", 2)
        val GIP = Currency("GIP", 2)
        val GMD = Currency("GMD", 2)
        val GNF = Currency("GNF", 0)
        val GTQ = Currency("GTQ", 2)
        val GYD = Currency("GYD", 2)
        val HKD = Currency("HKD", 2)
        val HNL = Currency("HNL", 2)
        val HRK = Currency("HRK", 2)
        val HTG = Currency("HTG", 2)
        val HUF = Currency("HUF", 2)
        val IDR = Currency("IDR", 2)
        val ILS = Currency("ILS", 2)
        val INR = Currency("INR", 2)
        val IQD = Currency("IQD", 3)
        val IRR = Currency("IRR", 2)
        val ISK = Currency("ISK", 0)
        val JMD = Currency("JMD", 2)
        val JOD = Currency("JOD", 3)
        val KES = Currency("KES", 2)
        val KGS = Currency("KGS", 2)
        val KHR = Currency("KHR", 2)
        val KMF = Currency("KMF", 0)
        val KPW = Currency("KPW", 2)
        val KRW = Currency("KRW", 0)
        val KYD = Currency("KYD", 2)
        val KZT = Currency("KZT", 2)
        val LAK = Currency("LAK", 2)
        val LBP = Currency("LBP", 2)
        val LKR = Currency("LKR", 2)
        val LRD = Currency("LRD", 2)
        val LSL = Currency("LST", 2)
        val LTL = Currency("LTL", 2)
        val LYD = Currency("LYD", 3)
        val MAD = Currency("MAD", 2)
        val MDL = Currency("MDL", 2)
        val MGA = Currency("MGA", 2)
        val MKD = Currency("MKD", 2)
        val MMK = Currency("MMK", 2)
        val MNT = Currency("MNT", 2)
        val MOP = Currency("MOP", 2)
        val MRO = Currency("MRO", 2)
        val MUR = Currency("MUR", 2)
        val MVR = Currency("MVR", 2)
        val MWK = Currency("MWK", 2)
        val MXN = Currency("MXK", 2)
        val MXV = Currency("MXV", 2)
        val MYR = Currency("MYR", 2)
        val MZN = Currency("MZN", 2)
        val NAD = Currency("NAD", 2)
        val NGN = Currency("NGN", 2)
        val NIO = Currency("NIO", 2)
        val NOK = Currency("NOK", 2)
        val NPR = Currency("NPR", 2)
        val NZD = Currency("NZD", 2)
        val OMR = Currency("OMR", 3)
        val PAB = Currency("PAB", 2)
        val PEN = Currency("PEN", 2)
        val PGK = Currency("PGK", 2)
        val PHP = Currency("PHP", 2)
        val PKR = Currency("PKR", 2)
        val PLN = Currency("PLN", 2)
        val PYG = Currency("PYG", 0)
        val QAR = Currency("QAR", 2)
        val RON = Currency("RON", 2)
        val RSD = Currency("RSD", 2)
        val RUB = Currency("RUB", 2)
        val RWF = Currency("RWF", 0)
        val SAR = Currency("SAR", 2)
        val SBD = Currency("SBD", 2)
        val SCR = Currency("SCR", 2)
        val SDG = Currency("SGD", 2)
        val SEK = Currency("SEK", 2)
        val SGD = Currency("SGD", 2)
        val SHP = Currency("SHP", 2)
        val SLL = Currency("SLL", 2)
        val SOS = Currency("SOS", 2)
        val SRD = Currency("SRD", 2)
        val SSP = Currency("SSP", 2)
        val STD = Currency("STD", 2)
        val SVC = Currency("SVC", 2)
        val SYP = Currency("SYP", 2)
        val SZL = Currency("SZL", 2)
        val THB = Currency("THB", 2)
        val TJS = Currency("TJS", 2)
        val TMT = Currency("TMT", 2)
        val TND = Currency("TND", 3)
        val TOP = Currency("TOP", 2)
        val TRY = Currency("TRY", 2)
        val TTD = Currency("TTD", 2)
        val TWD = Currency("TWD", 2)
        val TZS = Currency("TZS", 2)
        val UAH = Currency("UAH", 2)
        val UGX = Currency("UGX", 0)
        val USN = Currency("USN", 2)
        val UYI = Currency("UYI", 0)
        val UYU = Currency("UYU", 2)
        val UZS = Currency("UZS", 2)
        val VEF = Currency("VEF", 2)
        val VND = Currency("VND", 0)
        val VUV = Currency("VUV", 0)
        val WST = Currency("WST", 2)
        val XAF = Currency("XAF", 0)
        val XCD = Currency("XCD", 0)
        val XOF = Currency("XOF", 0)
        val XPF = Currency("XPF", 0)
        val YER = Currency("YER", 2)
        val ZAR = Currency("ZAR", 2)
        val ZMW = Currency("ZMW", 2)
        val ZWL = Currency("ZWL", 2)
    }
}

