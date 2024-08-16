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
    private boolean forQuiz;
    @NotNull
    @DateTimeFormat
    private String fromDate;
    private String label;
    @NotNull
    private String toDate;
    private String currentDate;
}
