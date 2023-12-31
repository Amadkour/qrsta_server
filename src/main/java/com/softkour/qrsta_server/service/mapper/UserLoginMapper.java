package com.softkour.qrsta_server.service.mapper;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    UserLoginMapper INSTANCE = Mappers.getMapper(UserLoginMapper.class);

    UserLoginResponse toDto(User user);
}
