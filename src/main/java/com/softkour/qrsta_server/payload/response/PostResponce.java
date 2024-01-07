package com.softkour.qrsta_server.payload.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostResponce {
    String id;
    String data;
    SessionDateAndStudentGrade session;
    long courseId;
    AbstractUser owner;
    SessionDateAndStudentGrade linkedSession;
    Set<String> media = new HashSet<>();
    int likesCount = 0;
    int dislikesCount = 0;
    Instant date;
    List<PostResponce> comments = new ArrayList<>();
    boolean isLiked;
    boolean isDislike;

}
