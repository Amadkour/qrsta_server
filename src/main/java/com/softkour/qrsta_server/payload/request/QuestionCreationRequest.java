package com.softkour.qrsta_server.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.Option;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class QuestionCreationRequest {
    private String title;

    private int grade;
    private Set<OptionCreationRequest> options = new HashSet<>();

}
