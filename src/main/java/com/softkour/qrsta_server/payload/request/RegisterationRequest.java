package com.softkour.qrsta_server.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softkour.qrsta_server.config.Constants;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@ToString
@Validated
public class RegisterationRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotNull
    private UserType userType;
    @NotNull
    private OrganizationType organizationName;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    @Size(min = 3, max = 50)
    private String address;

    @NotBlank
    @Pattern(regexp = Constants.Date_REGEX,message = "must be as 1996-09-22")
    @Size(min = 10, max = 10)
    private String birthDate;

    @NotBlank
    @Size(min = 9, max = 11)
//    @Pattern(regexp = Constants.PHONE_REGEX, message = "must be as 01XXXXXXXXX")
    private String phone;

    @NotBlank
    @Size(min = 9, max = 14,message = "must be 9 to 14 digits")
    private String nationalId;

    @NotBlank
    @Size(min = 6)
    private String macAddress;
}