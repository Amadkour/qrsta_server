package com.softkour.qrsta_server.payload.request;

import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Validated
public class UpdateUserRequest {

    private Long id;
    private String name;
    private OrganizationType organizationName;
    private String address;
    private String birthDate;
    private String nationalId;
    private String phoneNumber;
    private String phoneCode;

    public User toUser(User user) {
        user.setPhoneNumber(getPhoneNumber());
        user.setCountryCode(getPhoneCode());
        if (getName() != null)
            user.setName(getName());
        if (getAddress() != null)
            user.setAddress(getAddress());
        if (getNationalId() != null)
            user.setNationalId(getNationalId());
        if (getOrganizationName() != null)
            user.setOrganization(getOrganizationName());
        if (getBirthDate() != null)
            user.setDob(LocalDate.parse(getBirthDate()));
        return user;

    }

}