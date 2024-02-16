package com.softkour.qrsta_server.payload.response;

import java.time.LocalDate;

import com.softkour.qrsta_server.entity.enumeration.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {
    private long id;
    private String name;
    private UserType type;
    private String phoneNumber;
    private String token;
    private String address;
    private String imageURL;
    private String organization;
    private String nationalId;
    private String countryCode;
    private boolean validDevice;
    private LocalDate bod;

}
