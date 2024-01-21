package com.softkour.qrsta_server.payload.request;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class QuestionCreationRequest {
    private Long id;
    private String title;
    private int grade;
    private Set<OptionCreationRequest> options = new HashSet<>();

}
