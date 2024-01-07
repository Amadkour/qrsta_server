package com.softkour.qrsta_server.exception;

public class ClientException extends RuntimeException {
    String key;

    public ClientException(String key, String value) {
        super(value);
        this.key = key;

    }
}
