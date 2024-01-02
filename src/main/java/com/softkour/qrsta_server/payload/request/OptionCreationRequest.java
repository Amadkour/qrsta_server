package com.softkour.qrsta_server.payload.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class OptionCreationRequest {
    private String title;
    private Boolean isCorrectAnswer;
}
