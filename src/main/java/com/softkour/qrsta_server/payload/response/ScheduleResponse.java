package com.softkour.qrsta_server.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ScheduleResponse {
    private Long id;
    private String day;
    private String from;
    private String to;

}
