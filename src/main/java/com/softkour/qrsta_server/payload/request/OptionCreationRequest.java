package com.softkour.qrsta_server.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionCreationRequest {
    private String title;
    private Boolean isCorrectAnswer;
}
