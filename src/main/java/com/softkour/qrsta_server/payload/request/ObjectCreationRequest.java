package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
public class ObjectCreationRequest {
    @NotNull(message = "item can't be null")
    private String item;
    @Enumerated(value = EnumType.STRING)
    private SessionObjectType type;

    private long parentId;


}
