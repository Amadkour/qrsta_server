package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import com.softkour.qrsta_server.payload.response.SessionDetailsWithoutStudet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface SessionWithoutStudentsMapper {
    SessionWithoutStudentsMapper INSTANCE = Mappers.getMapper(SessionWithoutStudentsMapper.class);
SessionDetailsWithoutStudet toDTOs(Session session);
}
