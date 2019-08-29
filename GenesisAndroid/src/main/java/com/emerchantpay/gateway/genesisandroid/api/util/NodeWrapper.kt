package com.emerchantpay.gateway.genesisandroid.api.util


import java.io.Serializable
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

abstract class NodeWrapper : Serializable {

    abstract val elementName: String // TODO MDM Rename to getName

    val isSuccess: Boolean
        get() = elementName != "error"

    abstract val formParameters: Map<String, String>

    abstract val isBlank: Boolean

    abstract fun findAll(expression: String): List<NodeWrapper>

    fun findAllStrings(expression: String): List<String> {
        val strings = ArrayList<String>()

        for (node in findAll(expression)) {
            node.findString(".")?.let { strings.add(it) }
        }

        return strings
    }

    fun findBigDecimal(expression: String): BigDecimal? {
        val value = findString(expression)
        return if (value == null) null else BigDecimal(value)
    }

    fun findBoolean(expression: String): Boolean {
        val value = findString(expression)
        return java.lang.Boolean.valueOf(value)
    }

    fun findDate(expression: String): Calendar? {
        try {
            val dateString = findString(expression) ?: return null
            val dateFormat = SimpleDateFormat(DATE_FORMAT)
            dateFormat.timeZone = TimeZone.getTimeZone(UTC_DESCRIPTOR)
            val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_DESCRIPTOR))
            calendar.time = dateFormat.parse(dateString)
            return calendar
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    fun findDateTime(expression: String): Calendar? {
        try {
            val dateString = findString(expression) ?: return null
            val dateTimeFormat = SimpleDateFormat(DATE_TIME_FORMAT)
            dateTimeFormat.timeZone = TimeZone.getTimeZone(UTC_DESCRIPTOR)
            val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_DESCRIPTOR))
            calendar.time = dateTimeFormat.parse(dateString)
            return calendar
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    fun findInteger(expression: String): Int? {
        val value = findString(expression)
        return if (value == null) 0 else value.toInt()
    }

    abstract fun findFirst(expression: String): NodeWrapper?

    abstract fun findString(expression: String): String?

    fun findMap(expression: String): Map<String, String> {
        val map = HashMap<String, String>()

        for (mapNode in findAll(expression)) {
            map[StringUtils.underscore(mapNode.elementName)!!] = mapNode.findString(".")!!
        }

        return map
    }

    companion object {

        val DATE_FORMAT = "yyyy-MM-dd"
        val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val UTC_DESCRIPTOR = "UTC"
    }
}
