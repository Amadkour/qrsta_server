package com.softkour.qrsta_server.service.dto;

import com.softkour.qrsta_server.entity.enumeration.UserType;
import lombok.*;

@Data
public class UserLoginResponse {
    private String name;
    private UserType type;
    private String phoneNumber;
    private String token;
    private String address;
    private String macAddress;
    private String imageURL;
    private String organization;
    private String nationalId;

}
