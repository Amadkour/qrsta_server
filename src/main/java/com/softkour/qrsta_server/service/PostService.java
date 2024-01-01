package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Post;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.PostRepository;
import com.softkour.qrsta_server.repo.SessionRepository;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import com.softkour.qrsta_server.service.dto.UserLogo;
import com.softkour.qrsta_server.payload.mapper.UserLoginMapper;
import com.softkour.qrsta_server.payload.mapper.UserLogoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;

    public Post addPost(Post post) {
        post.setOwner(UserLoginMapper.INSTANCE.toDto(MyUtils.getCurrentUserSession(userRepository)));
        post.setDate(Instant.now());
        return postRepository.save(post);
    }

    public Post addComment(Post post, String parentPostId) {
        post.setOwner(UserLoginMapper.INSTANCE.toDto(MyUtils.getCurrentUserSession(userRepository)));
        post.setDate(Instant.now());
        Post parentPost = getPost(parentPostId);
        List<Post> comments = parentPost.getComments();
        comments.add(post);
        parentPost.setComments(comments);
        return postRepository.save(parentPost);
    }

    public Post addReplay(Post post, String parentPostId, String commentIndex) {
        int index = Integer.parseInt(commentIndex);
        post.setOwner(UserLoginMapper.INSTANCE.toDto(MyUtils.getCurrentUserSession(userRepository)));
        post.setDate(Instant.now());
        post.setComments(null);
        Post parentPost = getPost(parentPostId);
        List<Post> comments = parentPost.getComments();
        ///add replay
        Post comment = comments.get(index);
        List<Post> replays = comment.getComments();
        replays.add(post);
        ///update comment
        comment.setComments(replays);
        comments.remove(index);
        comments.add(index, comment);
        parentPost.setComments(comments);
        return postRepository.save(parentPost);
    }

    public Post getPost(String id) {
        return postRepository.findById(id).orElseThrow();
    }

    public UserLogo getCurrentLoggenUserLogo() {
        return UserLogoMapper.INSTANCE.toDto(MyUtils.getCurrentUserSession(userRepository));
    }
    ///==================================[like]===============================//

    //==========post

    public Post updatePostLike(String postId, boolean newValue) {
        Post post = getPost(postId);
        if (newValue)
            post.addLike(getCurrentLoggenUserLogo());
        else
            post.removeLike(getCurrentLoggenUserLogo());
        return postRepository.save(post);
    }

    //===========comment
    public Post updateCommentLike(String commentIndex, String postId, boolean newValue) {
        int index = Integer.parseInt(commentIndex);
        Post post = getPost(postId);
        //comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(index);
        //update
        if (newValue)
            comment.addLike(getCurrentLoggenUserLogo());
        else
            comment.removeLike(getCurrentLoggenUserLogo());
        comments.remove(index);
        comments.add(index,comment);
        post.setComments(comments);
        return postRepository.save(post);
    }
    //================replay
    public Post updateLikeToReplay(String replayIndex, String commentIndex, String postId, boolean newValue) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post post = getPost(postId);
        //comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(comIndex);
        //replay
        List<Post> replays = comment.getComments();
        Post replay = replays.get(repIndex);
        ///update
        if (newValue)
            replay.addLike(getCurrentLoggenUserLogo());
        else
            replay.removeLike(getCurrentLoggenUserLogo());
        replays.remove(repIndex);
        replays.add(repIndex, replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    ///==================================[dislike]===============================//

    //==========post

    public Post updatePostDislike(String postId, boolean newValue) {
        Post post = getPost(postId);
        if (newValue)
            post.addDislike(getCurrentLoggenUserLogo());
        else
            post.removeDislike(getCurrentLoggenUserLogo());
        return postRepository.save(post);
    }

    //===========comment
    public Post updateCommentDislike(String commentIndex, String postId, boolean newValue) {
        int index = Integer.parseInt(commentIndex);
        Post post = getPost(postId);
        //comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(index);
        //update
        if (newValue)
            comment.addDislike(getCurrentLoggenUserLogo());
        else
            comment.removeDislike(getCurrentLoggenUserLogo());
        comments.remove(index);
        comments.add(index,comment);
        post.setComments(comments);
        return postRepository.save(post);
    }
    //================replay
    public Post updateDislikeToReplay(String replayIndex, String commentIndex, String postId, boolean newValue) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post post = getPost(postId);
        //comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(comIndex);
        //replay
        List<Post> replays = comment.getComments();
        Post replay = replays.get(repIndex);
        ///update
        if (newValue)
            replay.addDislike(getCurrentLoggenUserLogo());
        else
            replay.removeDislike(getCurrentLoggenUserLogo());
        replays.remove(repIndex);
        replays.add(repIndex, replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    //===============================[ linked session ]===============================//
    public Post linkSessionToPost(String postId, Long sessionId) {
        Post p = getPost(postId);
        Session session = sessionRepository.getReferenceById(sessionId);
        p.setLinkedSession(session);
        return postRepository.save(p);
    }

    public Post linkSessionToComment(String postId, String commentIndex, Long sessionId) {
        int comIndex = Integer.parseInt(commentIndex);
        Post p = getPost(postId);
        Session session = sessionRepository.getReferenceById(sessionId);
        List<Post> comments = p.getComments();
        Post comment = comments.get(comIndex);
        comment.setLinkedSession(session);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        p.setComments(comments);
        return postRepository.save(p);
    }

    public Post linkSessionToReplay(String postId, String commentIndex, String replayIndex, Long sessionId) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post p = getPost(postId);
        Session session = sessionRepository.getReferenceById(sessionId);
        List<Post> comments = p.getComments();
        Post comment = comments.get(comIndex);
        //replay
        List<Post> replays = comment.getComments();
        Post replay = replays.get(repIndex);
        ///update
        replay.setLinkedSession(session);
        replays.remove(repIndex);
        replays.add(repIndex, replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        p.setComments(comments);
        return postRepository.save(p);
    }

}
