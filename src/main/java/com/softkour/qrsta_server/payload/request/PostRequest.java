package com.softkour.qrsta_server.payload.request;

import java.util.Set;

import com.softkour.qrsta_server.entity.enumeration.CourseType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostRequest {
    private String data;
    private String parentId;
    private Long courseId;
    private String commentIndex;
    private Set<String> media;
    private CourseType type;
    private Long sessionId;
}
