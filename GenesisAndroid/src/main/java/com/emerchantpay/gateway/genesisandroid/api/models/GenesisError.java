package com.emerchantpay.gateway.genesisandroid.api.models;

public class GenesisError {

    private Integer code;
    private String technicalMessage;
    private String message;

    public GenesisError() {
        super();
    }

    public GenesisError(Integer code, String message, String technicalMessage) {
        super();

        this.code = code;
        this.message = message;
        this.technicalMessage = technicalMessage;
    }

    public GenesisError(Integer code, String technicalMessage) {
        super();

        this.code = code;
        this.technicalMessage = technicalMessage;
    }

    public GenesisError(String message, String technicalMessage) {
        super();

        this.message = message;
        this.technicalMessage = technicalMessage;
    }

    public GenesisError(String message) {
        super();

        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }
}
