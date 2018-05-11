package com.emerchantpay.gateway.genesisandroid.api.models;

public class RiskParams {

    private String userId;
    private String sessionId;
    private String ssn;
    private String macAddress;
    private String userLevel;
    private String email;
    private String phone;
    private String remoteIp;
    private String serialNumber;
    private String panTail;
    private String bin;
    private String firstname;
    private String lastname;
    private String country;
    private String pan;
    private String forwardedIp;
    private String username;
    private String password;
    private String binName;
    private String binPhone;

    public RiskParams(String userId, String sessionId, String ssn, String macAddress,
                      String userLevel, String email, String phone, String remoteIp,
                      String serialNumber, String panTail, String bin, String firstname,
                      String lastname, String country, String pan, String forwardedIp, String username,
                      String password, String binName, String binPhone) {
        super();

        this.userId = userId;
        this.sessionId = sessionId;
        this.ssn = ssn;
        this.macAddress = macAddress;
        this.userLevel = userLevel;
        this.email = email;
        this.phone = phone;
        this.remoteIp = remoteIp;
        this.serialNumber = serialNumber;
        this.panTail = panTail;
        this.bin = bin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pan = pan;
        this.country = country;
        this.forwardedIp = forwardedIp;
        this.username = username;
        this.password = password;
        this.binName = binName;
        this.binPhone = binPhone;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSsn() {
        return ssn;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getPanTail() {
        return panTail;
    }

    public String getBin() {
        return bin;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCountry() {
        return country;
    }

    public String getPan() {
        return pan;
    }

    public String getForwardedIp() {
        return forwardedIp;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBinName() {
        return binName;
    }

    public String getBinPhone() {
        return binPhone;
    }
}
