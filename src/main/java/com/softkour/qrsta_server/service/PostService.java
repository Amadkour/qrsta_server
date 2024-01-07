package com.softkour.qrsta_server.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Post;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.repo.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    AuthService userService;
    @Autowired
    SessionService sessionService;

    public AbstractUser getCurrentUserAsAbstractUser() {
        return MyUtils.getCurrentUserSession(userService).toAbstractUser();
    }

    public List<Post> posts(long courseId) {
        return postRepository.findByCourseId(courseId);

    }

    public Post addPost(Post post) {
        post.setOwner(getCurrentUserAsAbstractUser());
        post.setDate(Instant.now());
        log.warn(post.toString());
        return postRepository.save(post);
    }

    public Post addComment(Post post, String parentPostId) {
        post.setOwner(getCurrentUserAsAbstractUser());
        post.setDate(Instant.now());
        Post parentPost = getPost(parentPostId);
        List<Post> comments = parentPost.getComments();
        comments.add(post);
        parentPost.setComments(comments);
        return postRepository.save(parentPost);
    }

    public Post addReplay(Post post, String parentPostId, String commentIndex) {
        int index = Integer.parseInt(commentIndex);
        post.setOwner(getCurrentUserAsAbstractUser());
        post.setDate(Instant.now());
        post.setComments(null);
        Post parentPost = getPost(parentPostId);
        List<Post> comments = parentPost.getComments();
        /// add replay
        Post comment = comments.get(index);
        List<Post> replays = comment.getComments();
        replays.add(post);
        /// update comment
        comment.setComments(replays);
        comments.remove(index);
        comments.add(index, comment);
        parentPost.setComments(comments);
        return postRepository.save(parentPost);
    }

    public Post getPost(String id) {
        return postRepository.findById(id).orElseThrow();
    }
    /// ==================================[like]===============================//

    // ==========post

    public Post updatePostLike(String postId, boolean newValue) {
        Post post = getPost(postId);
        if (newValue)
            post.addLike(getCurrentUserAsAbstractUser());
        else
            post.removeLike(getCurrentUserAsAbstractUser());
        return postRepository.save(post);
    }

    // ===========comment
    public Post updateCommentLike(String commentIndex, String postId, boolean newValue) {
        int index = Integer.parseInt(commentIndex);
        Post post = getPost(postId);
        // comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(index);
        // update
        if (newValue)
            comment.addLike(getCurrentUserAsAbstractUser());
        else
            comment.removeLike(getCurrentUserAsAbstractUser());
        comments.remove(index);
        comments.add(index, comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    // ================replay
    public Post updateLikeToReplay(String replayIndex, String commentIndex, String postId, boolean newValue) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post post = getPost(postId);
        // comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(comIndex);
        // replay
        List<Post> replays = comment.getComments();
        Post replay = replays.get(repIndex);
        /// update
        if (newValue)
            replay.addLike(getCurrentUserAsAbstractUser());
        else
            replay.removeLike(getCurrentUserAsAbstractUser());
        replays.remove(repIndex);
        replays.add(repIndex, replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    /// ==================================[dislike]===============================//

    // ==========post

    public Post updatePostDislike(String postId, boolean newValue) {
        Post post = getPost(postId);
        if (newValue)
            post.addDislike(getCurrentUserAsAbstractUser());
        else
            post.removeDislike(getCurrentUserAsAbstractUser());
        return postRepository.save(post);
    }

    // ===========comment
    public Post updateCommentDislike(String commentIndex, String postId, boolean newValue) {
        int index = Integer.parseInt(commentIndex);
        Post post = getPost(postId);
        // comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(index);
        // update
        if (newValue)
            comment.addDislike(getCurrentUserAsAbstractUser());
        else
            comment.removeDislike(getCurrentUserAsAbstractUser());
        comments.remove(index);
        comments.add(index, comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    // ================replay
    public Post updateDislikeToReplay(String replayIndex, String commentIndex, String postId, boolean newValue) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post post = getPost(postId);
        // comment
        List<Post> comments = post.getComments();
        Post comment = comments.get(comIndex);
        // replay
        List<Post> replays = comment.getComments();
        Post replay = replays.get(repIndex);
        /// update
        if (newValue)
            replay.addDislike(getCurrentUserAsAbstractUser());
        else
            replay.removeDislike(getCurrentUserAsAbstractUser());
        replays.remove(repIndex);
        replays.add(repIndex, replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    // ===============================[ linked session
    // ]===============================//
    public Post linkSessionToPost(String postId, Long sessionId) {
        Post p = getPost(postId);
        Session session = sessionService.getReferenceById(sessionId);
        p.setLinkedSessionId(session.getId());
        return postRepository.save(p);
    }

    public Post linkSessionToComment(String postId, String commentIndex, Long sessionId) {
        int comIndex = Integer.parseInt(commentIndex);
        Post p = getPost(postId);
        Session session = sessionService.getReferenceById(sessionId);
        List<Post> comments = p.getComments();
        Post comment = comments.get(comIndex);
        comment.setLinkedSessionId(session.getId());
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        p.setComments(comments);
        return postRepository.save(p);
    }

    public Post linkSessionToReplay(String postId, String commentIndex, String replayIndex, Long sessionId) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post p = getPost(postId);
        Session session = sessionService.getReferenceById(sessionId);
        List<Post> comments = p.getComments();
        Post comment = comments.get(comIndex);
        // replay
        List<Post> replays = comment.getComments();
        Post replay = replays.get(repIndex);
        /// update
        replay.setLinkedSessionId(session.getId());
        replays.remove(repIndex);
        replays.add(repIndex, replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex, comment);
        p.setComments(comments);
        return postRepository.save(p);
    }

}
