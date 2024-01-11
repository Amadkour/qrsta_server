package com.softkour.qrsta_server.payload.request;

import java.time.Instant;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.service.OTPService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ParentRegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotBlank
    private String countryCode;
    @NotBlank
    @Size(min = 7, max = 10)
    private String phone;

    public User toUser(OTPService otpService) {
        User user = new User();
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setName(this.getName());
        user.setPhoneNumber(this.getPhone());
        user.setOtp(otp.get());
        user.setCountryCode(this.getCountryCode());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        user.setType(UserType.PARENT);
        return user;

    }
}
