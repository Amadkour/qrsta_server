package com.softkour.qrsta_server.payload.request;

import java.time.Instant;
import java.time.LocalDate;
import java.util.function.Supplier;

import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

import com.softkour.qrsta_server.config.Constants;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.service.OTPService;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private String countryCode;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    @Size(min = 3, max = 50)
    private String address;

    @NotBlank
    @Pattern(regexp = Constants.Date_REGEX, message = "must be as 1996-09-22")
    @Size(min = 10, max = 10)
    private String birthDate;

    @NotBlank
    @Size(max = 14, min = 10)
    @Unique()
    // @Pattern(regexp = Constants.PHONE_REGEX, message = "must be as 01XXXXXXXXX")
    private String phone;

    @NotBlank
    @Unique()

    @Size(min = 9, max = 14, message = "must be 9 to 14 digits")
    private String nationalId;

    @NotBlank
    @Unique()
    @Size(min = 6)
    private String macAddress;

    public User toUser(OTPService otpService) {
        User user = new User();
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setName(getName());
        user.setCountryCode(getCountryCode());
        user.setRegisterMacAddress(getMacAddress());
        user.setAddress(getAddress());
        user.setPhoneNumber(getPhone());
        user.setDob(LocalDate.parse(getBirthDate()));
        user.setNationalId(getNationalId());
        user.setPassword(new BCryptPasswordEncoder().encode(this.getPassword()));
        user.setOtp(otp.get());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        user.setType(getUserType());
        if (getUserType() == UserType.TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setOrganization(getOrganizationName());
            user.setTeacher(teacher);
        } else if (getUserType() == UserType.STUDENT) {
            Student student = new Student();
            user.setStudent(student);
        }
        user.setDob(LocalDate.parse(getBirthDate()));
        return user;

    }

}