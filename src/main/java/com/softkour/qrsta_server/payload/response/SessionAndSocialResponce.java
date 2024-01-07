package com.softkour.qrsta_server.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionAndSocialResponce {
    public List<SessionDateAndStudentGrade> sessions;
    public List<PostResponce> posts;
}
