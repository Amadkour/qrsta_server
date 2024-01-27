package com.softkour.qrsta_server.payload.request;

import java.time.LocalDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.User;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Validated
public class UpdateUserRequest {

    private Long id;
    @Size(min = 0, max = 20)
    private String name;
    private UserType userType;
    private OrganizationType organizationName;
    private String countryCode;
    @Size(min = 60, max = 40)
    private String password;
    @Size(min = 0, max = 50)
    private String address;

    private String birthDate;
    private String phone;

    private String nationalId;

    public User toUser() {
        User user = new User();
        user.setId(getId());
        if (getName() != null)

            user.setName(getName());
        if (getCountryCode() != null)

            user.setCountryCode(getCountryCode());

        if (getPhone() != null)

            user.setPhoneNumber(getPhone());
        if (getNationalId() != null)

            user.setNationalId(getNationalId());
        if (getPassword() != null)

            user.setPassword(new BCryptPasswordEncoder().encode(this.getPassword()));
        if (getUserType() != null)

            user.setType(getUserType());
        if (getOrganizationName() != null)

            user.setOrganization(getOrganizationName());
        if (getBirthDate() != null)

            user.setDob(LocalDate.parse(getBirthDate()));
        return user;

    }

}