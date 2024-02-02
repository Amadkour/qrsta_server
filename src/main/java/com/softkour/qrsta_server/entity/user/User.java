package com.softkour.qrsta_server.entity.user;

import java.time.Instant;
import java.time.LocalDate;
import com.softkour.qrsta_server.entity.enumeration.DeviceType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@MappedSuperclass
@Setter
@Getter
public class User extends AbstractAuditingEntity {
    @NotNull
    @Column(nullable = false)
    private String name;
    @NotNull
    @Column(nullable = false, unique = true)
    @Size(max = 10, min = 7)
    private String phoneNumber;

    @Column(nullable = true, unique = true)
    @Size(max = 14, min = 9)
    private String nationalId;

    private String otp;
    @NotNull
    private String countryCode;

    @Column()
    private LocalDate dob;

    @Column()
    private String registerMacAddress;
    @Column()
    private String loginMacAddress;

    @Column()
    private String imageUrl;

    @Column()
    private String address;

    @Column(columnDefinition = "boolean default false")
    private boolean isActive;
    @Column(columnDefinition = "boolean default true")
    private boolean needToReplace;
    @Column(columnDefinition = "boolean default false")
    private boolean isLogged;
    @Column(columnDefinition = "integer default 0")
    private int logoutTimes;
    @Column()
    private String password;
    @Column()
    private String fcmToken;
    @Column()
    private DeviceType deviceType;
    @Column
    private Instant ExpireOTPDateTime;
    @Column
    private Instant ExpirePasswordDate;


    public AbstractUser toAbstractUser() {
        return new AbstractUser(
                getId(), getName(), getType(), getImageUrl(), getPhoneNumber());
    }

    public UserType getType() {
        if (this instanceof Parent) {
            return UserType.OBSERVER;
        } else if (this instanceof Teacher) {
            return UserType.TEACHER;
        } else {
            return UserType.STUDENT;
        }
    }

    public UserLoginResponse toUserLoginResponse() {
        return new UserLoginResponse(
                this.getName(),
                getType(),
                this.getPhoneNumber(),
                "token",
                this.getAddress(),
                this.getImageUrl(),
                null,
                this.getNationalId(),
                this.getCountryCode(),
                getLoginMacAddress().equalsIgnoreCase(getRegisterMacAddress()),
                getDob());

    }

    public UserLoginResponse toUpdateResponse() {
        return new UserLoginResponse(
                this.getName(),
                getType(),
                this.getPhoneNumber(),
                null,
                this.getAddress(),
                this.getImageUrl(),
                null,
                this.getNationalId(),
                this.getCountryCode(),
                true,
                getDob());
    }
}