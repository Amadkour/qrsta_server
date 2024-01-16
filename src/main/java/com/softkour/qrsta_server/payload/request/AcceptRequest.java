package com.softkour.qrsta_server.payload.request;

import com.google.firebase.database.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AcceptRequest {
    @NotNull
    private Long id;
    private String macAddress;

}
