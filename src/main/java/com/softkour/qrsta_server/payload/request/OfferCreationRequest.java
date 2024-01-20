package com.softkour.qrsta_server.payload.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OfferCreationRequest {
    List<Long> courseIds = new ArrayList<>();
    private String endDate;
    private int months;
    private String cost;
}
