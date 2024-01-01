package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface SessionStudentsMapper {
    SessionStudentsMapper INSTANCE = Mappers.getMapper(SessionStudentsMapper.class);
SessionDetailsStudent toDTOs(Session session);
}
