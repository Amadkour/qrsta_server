package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.response.UserLogo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserLogoMapper {
    UserLogoMapper INSTANCE = Mappers.getMapper(UserLogoMapper.class);
    UserLogo toDto(User user);


}
