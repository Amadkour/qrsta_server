package com.softkour.qrsta_server.payload.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateResponse {
    private String name;
    private String phoneNumber;
    private String address;
    private String imageURL;
    private String organization;
    private String nationalId;
    private String countryCode;
    private LocalDate bod;

}
