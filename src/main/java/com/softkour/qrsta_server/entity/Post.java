package com.softkour.qrsta_server.entity;


import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
public class Post {
    @Id
    String id;
    String data;
    Session session;
    UserLoginResponse owner;
    List<String> media= new ArrayList<>();
    List<UserLoginResponse> likes= new ArrayList<>();
    List<UserLoginResponse> dislikes= new ArrayList<>();
    Instant date;
    List<Post> comments= new ArrayList<>();
}
