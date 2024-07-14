package com.softkour.qrsta_server.payload.response;

import java.time.Instant;

import com.softkour.qrsta_server.entity.enumeration.AssignmentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private Instant dueDate;
    private boolean finish;
    private AssignmentType type;

}
