package com.softkour.qrsta_server.request;

import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
