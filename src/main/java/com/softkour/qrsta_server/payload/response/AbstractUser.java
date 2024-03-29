package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.enumeration.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AbstractUser {
    private Long id;
    private String name;
    private UserType type;
    private String imageURL;
    private String phone;

}
