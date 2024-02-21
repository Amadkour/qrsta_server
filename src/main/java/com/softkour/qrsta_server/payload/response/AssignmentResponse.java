package com.softkour.qrsta_server.payload.response;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AssignmentResponse {
    private Long id;
    private List<GroupAssignmentResponse> groups;
    private String title;
    private String description;
    private Instant dueDate;
    private List<String> mediaUrls;

}
