package com.emerchantpay.gateway.genesisandroid;

import com.emerchantpay.gateway.genesisandroid.api.models.Currency;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CurrencyUnitTest {

    private BigDecimal amount;
    private String currency;

    @Test
    public void testTransformAmountToExponent() {

        amount = new BigDecimal("10.00");
        currency = Currency.USD.getCurrency();

        if (amount != null && currency != null) {

            Currency curr = new Currency();

            curr.setAmountToExponent(amount, currency);
            amount = curr.getAmount();
        }

        assertEquals(amount, new BigDecimal("1000"));
    }

    @Test
    public void testTransformExponentToAmount() {

        amount = new BigDecimal("1000.00");
        currency = Currency.USD.getCurrency();

        if (amount != null && currency != null) {
            Currency currency = new Currency();

            currency.setExponentToAmount(amount, this.currency);
            amount = currency.getAmount();
        }

        assertEquals(amount, new BigDecimal("10.00"));
    }

    @Test
    public void testGetCurrency() {
        assertEquals(Currency.USD.getCurrency(), "USD");
    }

    @Test
    public void testFindCurrencyByName() throws IllegalAccessException {
        Currency currency = new Currency();

        assertEquals(currency.findCurrencyByName("USD").getCurrency(), Currency.USD.getCurrency());
        assertEquals(currency.getExponent("USD"), Currency.USD.getExponent());
    }

    @Test
    public void testGetExponent() {
        Currency currency = new Currency();

        assertEquals(currency.getExponent("USD"), Currency.USD.getExponent());
    }

    @Test
    public void testGetCurrencies() throws IllegalAccessException {
        Currency currency = new Currency();
        ArrayList<String> currencyList = currency.getCurrencies();

        assertEquals(currency.getCurrencies(), currencyList);
    }
}
