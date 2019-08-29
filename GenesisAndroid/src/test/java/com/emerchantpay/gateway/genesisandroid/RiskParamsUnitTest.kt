package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.models.RiskParams

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals

class RiskParamsUnitTest {

    private var riskParams: RiskParams? = null

    @Before
    fun setup() {
        riskParams = RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
                "987-65-4320", "12-34-56-78-9A-BC", "123456",
                "emil@example.com", "+49301234567", "245.253.2.12",
                "10000000000", "1234", "100000000", "John",
                "Doe", "US", "test", "245.25 3.2.12",
                "test", "test123456", "Bin name",
                "+49301234567")
    }

    @Test
    fun testRiskParams() {
        assertEquals(riskParams!!.userId, "1002547")
        assertEquals(riskParams!!.bin, "100000000")
        assertEquals(riskParams!!.username, "test")
        assertEquals(riskParams!!.password, "test123456")
        assertEquals(riskParams!!.firstname, "John")
        assertEquals(riskParams!!.lastname, "Doe")
        assertEquals(riskParams!!.userLevel, "123456")
        assertEquals(riskParams!!.country, "US")
        assertEquals(riskParams!!.pan, "test")
        assertEquals(riskParams!!.email, "emil@example.com")
        assertEquals(riskParams!!.phone, "+49301234567")
    }
}
