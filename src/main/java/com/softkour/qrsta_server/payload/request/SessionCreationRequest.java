package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.enumeration.DayType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionCreationRequest {
    @NotNull
    private long courseId;

    @NotNull
    private String fromDate;
    @NotNull
    private String toDate;
}
