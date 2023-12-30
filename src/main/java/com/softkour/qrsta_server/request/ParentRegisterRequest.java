package com.softkour.qrsta_server.request;

import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ParentRegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotBlank
    @Size(min = 11, max = 11)
    private String phone;
}
