package com.softkour.qrsta_server.request;

import com.softkour.qrsta_server.entity.enumeration.CourseType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class PostRequest {
    private String data;
    private String parentId;
    private String commentIndex;
    private List<String> media;
    private CourseType type;
    private Long sessionId;
}
