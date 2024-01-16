package com.softkour.qrsta_server.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.payload.response.PostResponce;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.SessionService;

import jakarta.persistence.Id;
import lombok.Data;

@Document
@Data
public class Post {
    @Id
    String id;
    String data;
    long sessionId;
    long courseId;
    AbstractUser owner;
    CourseType type;
    private long linkedSessionId;
    Set<String> media = new HashSet<>();
    Set<AbstractUser> likes = new HashSet<>();
    Set<AbstractUser> dislikes = new HashSet<>();
    Instant date;
    List<Post> comments = new ArrayList<>();

    public Set<AbstractUser> removeLike(AbstractUser user) {
        likes.removeIf((e) -> e.getId() == user.getId());
        return likes;
    }

    public Set<AbstractUser> addLike(AbstractUser user) {
        if (!likes.stream().anyMatch((e) -> e.getId() == user.getId()))
            likes.add(user);
        return likes;
    }

    public Set<AbstractUser> removeDislike(AbstractUser user) {
        dislikes.removeIf((e) -> e.getId() == user.getId());
        return dislikes;
    }

    public Set<AbstractUser> addDislike(AbstractUser user) {
        if (!dislikes.stream().anyMatch((e) -> e.getId() == user.getId()))
            dislikes.add(user);
        return dislikes;
    }

    public PostResponce toPostResponce(SessionService sessionService, AuthService authService) {
        AbstractUser currentUser = MyUtils.getCurrentUserSession(authService).toAbstractUser();
        PostResponce response = new PostResponce();
        response.setComments(
                this.getComments().stream().map((comment) -> comment.toPostResponce(sessionService, authService))
                        .toList());
        response.setData(this.getData());
        response.setDate(this.getDate());
        response.setId(this.getId());
        response.setOwner(this.getOwner());
        response.setMedia(this.getMedia());
        response.setLikesCount(this.getLikes().size());
        response.setDislikesCount(this.getDislikes().size());
        response.setLiked(this.getLikes().stream().anyMatch((e) -> e.getId() == currentUser.getId()));
        response.setDislike(this.getDislikes().stream().anyMatch((e) -> e.getId() == currentUser.getId()));
        try {
            response.setLinkedSession(sessionService.findOne(this.getLinkedSessionId()).toSessionDateAndStudentGrade());
        } catch (Exception e) {
        }
        try {
            response.setSession(sessionService.findOne(this.getSessionId()).toSessionDateAndStudentGrade());
        } catch (Exception e) {
        }

        return response;
    }

}
