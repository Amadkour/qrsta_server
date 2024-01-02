package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.response.UserLogo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public class UserLogoMapper {

   public UserLogo toDto(User user) {
        return new UserLogo(
                user.getId(), user.getName(), user.getType(), user.getImageUrl()
        );
    }


}
