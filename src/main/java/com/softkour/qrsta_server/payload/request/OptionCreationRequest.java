package com.softkour.qrsta_server.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OptionCreationRequest {
    private String title;
    private Boolean isCorrectAnswer;
}
