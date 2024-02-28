package com.softkour.qrsta_server.exception;

public class ClientException extends RuntimeException {
    public String key;
    public Integer code;
    public String additionalString;

    public ClientException(String key, String value, Integer code) {
        super(value);
        this.key = key;
        this.code = code;

    }

    public ClientException(String key, String value, Integer code, String additionalString) {
        super(value);
        this.key = key;
        this.code = code;
        this.additionalString = additionalString;

    }

    public ClientException(String key, String value) {
        super(value);
        this.key = key;
    }
}
