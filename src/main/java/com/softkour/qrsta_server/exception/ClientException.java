package com.softkour.qrsta_server.exception;

public class ClientException extends RuntimeException {
    String key;
    Integer code;

    public ClientException(String key, String value, Integer code) {
        super(value);
        this.key = key;
        this.code = code;

    }
    public ClientException(String key, String value) {
        super(value);
        this.key = key;
    }
}
