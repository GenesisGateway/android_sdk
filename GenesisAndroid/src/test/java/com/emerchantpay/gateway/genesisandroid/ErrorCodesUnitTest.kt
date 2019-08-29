package com.emerchantpay.gateway.genesisandroid

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorCodes

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals

class ErrorCodesUnitTest {

    private var errorCodes: ErrorCodes? = null

    @Before
    fun setup() {
        errorCodes = ErrorCodes.AUTHENTICATION_ERROR
    }

    @Test
    fun testErrorCodes() {
        assertEquals(errorCodes!!.getCode(), 110)
        assertEquals(errorCodes!!.findByCode(110), ErrorCodes.AUTHENTICATION_ERROR)
    }
}
