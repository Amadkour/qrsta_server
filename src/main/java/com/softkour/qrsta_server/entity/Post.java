package com.softkour.qrsta_server.entity;


import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import com.softkour.qrsta_server.service.dto.UserLogo;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.stream.events.Comment;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document
@Data
public class Post {
    @Id
    String id;
    String data;
    Session session;
    UserLoginResponse owner;
    Session linkedSession;
    Set<String> media= new HashSet<>();
    Set<UserLogo> likes= new HashSet<>();
    Set<UserLogo> dislikes= new HashSet<>();
    Instant date;
    List<Post> comments= new ArrayList<>();
    public  Set<UserLogo> removeLike(UserLogo user){
        likes.remove(user);
        return  likes;
    }
    public  Set<UserLogo> addLike(UserLogo user){
        likes.add(user);
        return  likes;
    }
    public  Set<UserLogo> removeDislike(UserLogo user){
        dislikes.remove(user);
        return  dislikes;
    }
    public  Set<UserLogo> addDislike(UserLogo user){
        dislikes.add(user);
        return  dislikes;
    }

}
