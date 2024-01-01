package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.enumeration.CourseType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class PostRequest {
    private String data;
    private String parentId;
    private String commentIndex;
    private Set<String> media;
    private CourseType type;
    private Long sessionId;
}
