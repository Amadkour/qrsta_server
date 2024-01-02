package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;

public class UserLoginMapper {
   public UserLoginResponse toDto(User user) {
        return new UserLoginResponse(
                user.getName(),
                user.getType(),
                user.getPhoneNumber(),
                "token",
                user.getAddress(),
                user.getMacAddress(),
                user.getImageUrl(),
                (user.getOrganization() == null) ? null : user.getOrganization().name(),
                user.getNationalId()
        );
    }
}
