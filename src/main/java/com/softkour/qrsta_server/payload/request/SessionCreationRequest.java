package com.softkour.qrsta_server.payload.request;
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
    private String currentDate;
}
