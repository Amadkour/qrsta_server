package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.enumeration.CourseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class CourseCreationRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    private double cost;
    @NotBlank
    private CourseType courseType;
    @NotBlank
    List<ScheduleRequest> schedules;
}
