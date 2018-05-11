package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.models.RiskParams;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RiskParamsUnitTest {

    private RiskParams riskParams;

    @Before
    public void setup() {
        riskParams = new RiskParams("1002547", "1DA53551-5C60-498C-9C18-8456BDBA74A9",
                "987-65-4320", "12-34-56-78-9A-BC", "123456",
                "emil@example.com", "+49301234567", "245.253.2.12",
                "10000000000", "1234", "100000000", "John",
                "Doe", "US", "test", "245.25 3.2.12",
                "test", "test123456", "Bin name",
                "+49301234567");
    }

    @Test
    public void testRiskParams() {
        assertEquals(riskParams.getUserId(), "1002547");
        assertEquals(riskParams.getBin(), "100000000");
        assertEquals(riskParams.getUsername(), "test");
        assertEquals(riskParams.getPassword(), "test123456");
        assertEquals(riskParams.getFirstname(), "John");
        assertEquals(riskParams.getLastname(), "Doe");
        assertEquals(riskParams.getUserLevel(), "123456");
        assertEquals(riskParams.getCountry(), "US");
        assertEquals(riskParams.getPan(), "test");
        assertEquals(riskParams.getEmail(), "emil@example.com");
        assertEquals(riskParams.getPhone(), "+49301234567");
    }
}
