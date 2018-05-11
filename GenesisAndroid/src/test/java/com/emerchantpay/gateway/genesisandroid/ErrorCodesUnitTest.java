package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.constants.ErrorCodes;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ErrorCodesUnitTest {

    private ErrorCodes errorCodes;

    @Before
    public void setup() {
         errorCodes = ErrorCodes.AUTHENTICATION_ERROR;
    }

    @Test
    public void testErrorCodes() {
        assertEquals(errorCodes.getCode(), 110);
        assertEquals(errorCodes.findByCode(110), ErrorCodes.AUTHENTICATION_ERROR);
    }
}
