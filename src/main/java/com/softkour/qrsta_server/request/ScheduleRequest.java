package com.softkour.qrsta_server.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScheduleRequest {
    @NotBlank
    @Size(min = 7, max = 8)
    private String day;
    @NotBlank
    @Size(min = 7, max = 8)
    private String from;
    @NotBlank
    @Size(min = 7, max = 8)
    private String to;
}
