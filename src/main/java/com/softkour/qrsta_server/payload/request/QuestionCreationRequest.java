package com.softkour.qrsta_server.payload.request;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreationRequest {
    private Long id;
    private String title;
    private int grade;
    private Set<OptionCreationRequest> options = new HashSet<>();

}
