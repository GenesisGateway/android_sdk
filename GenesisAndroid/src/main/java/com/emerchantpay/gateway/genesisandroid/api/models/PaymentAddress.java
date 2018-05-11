package com.emerchantpay.gateway.genesisandroid.api.models;

public class PaymentAddress {

    private String firstname;
    private String lastname;
    private String address1;
    private String address2;
    private String zipCode;
    private String city;
    private String state;
    private Country country;

    public PaymentAddress(String firstname, String lastname, String address1, String address2,
                          String zipCode, String city, String state, Country country) {
        super();

        this.firstname = firstname;
        this.lastname = lastname;
        this.address1 = address1;
        this.address2 = address2;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountryName() {
        return country.getCountryName();
    }

    public String getCountryCode() {
        return country.getCode();
    }
}
