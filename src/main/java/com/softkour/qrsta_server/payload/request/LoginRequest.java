package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.enumeration.DeviceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 10, max = 7)
    private String phone;

    @NotBlank
    private String countryCode;

    @NotBlank
    @Size(min = 20)
    private String macAddress;

    @NotBlank
    private DeviceType deviceType;
    @NotBlank
    private String fvmToken;
}