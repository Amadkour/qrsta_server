package com.softkour.qrsta_server.payload.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GroupAssignmentResponse {
    private Long id;
    private List<AbstractUser> students;
    private List<Double> degrees;
    private List<String> mediaUrls;

}
