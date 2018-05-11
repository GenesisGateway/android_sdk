package com.emerchantpay.gateway.genesisandroid.api.models;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Currency {

    private String currency;
    private Integer exponent;
    private BigDecimal convertedAmount;

    private HashMap<String, Integer> currenciesMap = new HashMap<String, Integer>();
    private ArrayList<String> currencyList = new ArrayList<String>();

    public Currency() {
        super();
    }

    public Currency(String currency, Integer exponent) {
        this.currency = currency;
        this.exponent = exponent;
    }

    public static Currency USD = new Currency("USD", 2);
    public static Currency EUR = new Currency("EUR", 2);
    public static Currency CNY = new Currency("CNY", 2);
    public static Currency GBP = new Currency("GBP", 2);
    public static Currency KWD = new Currency("KWD", 3);
    public static Currency JPY = new Currency("JPY", 0);

    public Integer getExponent(String currency) {
        Field field = null;
        Currency fieldValue = null;

        Class<? extends Currency> c = this.getClass();

        try {
            field = c.getField(currency);
            fieldValue = (Currency) field.get(this);
        } catch (NoSuchFieldException e) {
            Log.e("No Such Field Exception", e.toString());
        } catch (SecurityException e) {
            Log.e("Security Exception", e.toString());
        } catch (IllegalArgumentException e) {
            Log.e("Illegal Exception", e.toString());
        } catch (IllegalAccessException e) {
            Log.e("Illegal Exception", e.toString());
        }

        Integer exp = fieldValue.findCurrency(fieldValue).exponent;

        return exp;
    }

    public BigDecimal setAmountToExponent(BigDecimal amount, String currency) {

        exponent = getExponent(currency);

        if (exponent > 0) {
            BigDecimal multiplyExp = new BigDecimal(Math.pow(10, exponent), MathContext.DECIMAL64);

            convertedAmount = amount.multiply(multiplyExp).setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            convertedAmount = amount.setScale(0, BigDecimal.ROUND_DOWN);
        }

        return convertedAmount;
    }

    public BigDecimal setExponentToAmount(BigDecimal amount, String currency) {

        exponent = getExponent(currency);

        if (exponent > 0) {
            BigDecimal multiplyExp = new BigDecimal(Math.pow(10, exponent), MathContext.DECIMAL64);

            convertedAmount = amount.divide(multiplyExp);
        } else {
            convertedAmount = amount;
        }

        return convertedAmount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Integer getExponent() {
        return this.exponent;
    }

    public Currency findCurrency(Currency currency) {
        return currency;
    }

    public Currency findCurrencyByName(String name) throws IllegalAccessException {

        currencyList = getCurrencies();

        Integer exponent = currenciesMap.get(name);

        for (String currencyCode: currencyList) {
            if (currencyCode.equals(name)) {
                return new Currency(currencyCode, exponent);
            }
        }

        return null;
    }

    public BigDecimal getAmount() {

        return convertedAmount;
    }

    // Get Currencies
    public ArrayList<String> getCurrencies() throws IllegalAccessException {

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                currenciesMap.put(((Currency) f.get(this)).getCurrency(),
                        ((Currency) f.get(this)).getExponent());
                currencyList.add(((Currency) f.get(this)).getCurrency());
            }
        }

        // Sort Currencies
        Collections.sort(currencyList);

        return currencyList;
    }
}

