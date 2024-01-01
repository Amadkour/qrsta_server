package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.enumeration.DayType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class SessionCreationRequest {
    @NotNull
    private long courseId;

    @NotNull
    @DateTimeFormat
    private String fromDate;
    @NotNull
    private String toDate;
}
