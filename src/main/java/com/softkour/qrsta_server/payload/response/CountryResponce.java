package com.softkour.qrsta_server.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CountryResponce {
    private String name;
    private String imageUrl;
    private String phoneCode;
    private String sidMax;
    private String sidMin;
    private String phoneLength;
    private String phoneStart;

}
