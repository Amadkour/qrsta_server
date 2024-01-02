package com.softkour.qrsta_server.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Setter
@Getter
public class OfferCreationRequest {
    List<Long> courseIds=new ArrayList<>();
    private String endDate;
    private int months;
    private double cost;
}
