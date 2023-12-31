package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Post;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.PostRepository;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import com.softkour.qrsta_server.service.mapper.UserLoginMapper;
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

    public Post addPost(Post post) {
        post.setOwner(UserLoginMapper.INSTANCE.toDto(MyUtils.getCurrentUserSession(userRepository)));
        post.setDate(Instant.now());
        return postRepository.save(post);
    }

    public Post addComment(Post post, String parentPostId) {
        post.setOwner(UserLoginMapper.INSTANCE.toDto(MyUtils.getCurrentUserSession(userRepository)));
        post.setDate(Instant.now());
        Post parentPost = getPost(parentPostId).orElseThrow();
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
        Post parentPost = getPost(parentPostId).orElseThrow();
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

    public Optional<Post> getPost(String id) {
        return postRepository.findById(id);
    }
    ///==================================[like]===============================//

    //==========post
    public Post addLikeToPost(String postId) {
      Post post=  getPost(postId).orElseThrow();
      User user=MyUtils.getCurrentUserSession(userRepository);
      List<UserLoginResponse> list=post.getLikes();
      list.add(UserLoginMapper.INSTANCE.toDto(user));
      post.setLikes(list);
      return postRepository.save(post);
    }
    public Post removeLikeFromPost(String postId) {
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<UserLoginResponse> list=post.getLikes();
        list.removeIf(u-> Objects.equals(u.getNationalId(), user.getNationalId()));
        post.setLikes(list);
        return postRepository.save(post);
    }
    //===========comment
    public Post addLikeToComment(String commentIndex,String postId) {
        int index = Integer.parseInt(commentIndex);
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<Post> comments=post.getComments();
        Post comment=comments.get(index);
        List<UserLoginResponse> list=comment.getLikes();
        list.add(UserLoginMapper.INSTANCE.toDto(user));
        comment.setLikes(list);
        comments.remove(index);
        post.setComments(comments);
        return postRepository.save(post);
    }
    public Post removeLikeFromComment(String commentIndex,String postId) {
        int index = Integer.parseInt(commentIndex);
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<Post> comments=post.getComments();
        Post comment=comments.get(index);
        List<UserLoginResponse> list=comment.getLikes();
        list.removeIf(u-> Objects.equals(u.getNationalId(), user.getNationalId()));
        comment.setLikes(list);
        comments.remove(index);
        post.setComments(comments);
        return postRepository.save(post);
    }
    //================replay
    public Post addLikeToReplay(String replayIndex,String commentIndex,String postId) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        //comment
        List<Post> comments=post.getComments();
        Post comment=comments.get(comIndex);
        //replay
        List<Post> replays=comment.getComments();
        Post replay=replays.get(repIndex);
        List<UserLoginResponse> list=replay.getLikes();

        ///update
        list.add(UserLoginMapper.INSTANCE.toDto(user));
        replay.setLikes(list);
        replays.remove(repIndex);
        replays.add(repIndex,replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex,comment);
        post.setComments(comments);
        return postRepository.save(post);
    }

    ///==================================[dislike]===============================//

    //==========post
    public Post addDislikeToPost(String postId) {
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<UserLoginResponse> list=post.getDislikes();
        list.add(UserLoginMapper.INSTANCE.toDto(user));
        post.setDislikes(list);
        return postRepository.save(post);
    }
    public Post removeDislikeFromPost(String postId) {
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<UserLoginResponse> list=post.getDislikes();
        list.removeIf(u-> Objects.equals(u.getNationalId(), user.getNationalId()));
        post.setDislikes(list);
        return postRepository.save(post);
    }
    //===========comment
    public Post addDislikeToComment(String commentIndex,String postId) {
        int index = Integer.parseInt(commentIndex);
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<Post> comments=post.getComments();
        Post comment=comments.get(index);
        List<UserLoginResponse> list=comment.getDislikes();
        list.add(UserLoginMapper.INSTANCE.toDto(user));
        comment.setDislikes(list);
        comments.remove(index);
        post.setComments(comments);
        return postRepository.save(post);
    }
    public Post removeDislikeFromComment(String commentIndex,String postId) {
        int index = Integer.parseInt(commentIndex);
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        List<Post> comments=post.getComments();
        Post comment=comments.get(index);
        List<UserLoginResponse> list=comment.getDislikes();
        list.removeIf(u-> Objects.equals(u.getNationalId(), user.getNationalId()));
        comment.setDislikes(list);
        comments.remove(index);
        post.setComments(comments);
        return postRepository.save(post);
    }
    //================replay
    public Post addDislikeToReplay(String replayIndex,String commentIndex,String postId) {
        int comIndex = Integer.parseInt(commentIndex);
        int repIndex = Integer.parseInt(replayIndex);
        Post post=  getPost(postId).orElseThrow();
        User user=MyUtils.getCurrentUserSession(userRepository);
        //comment
        List<Post> comments=post.getComments();
        Post comment=comments.get(comIndex);
        //replay
        List<Post> replays=comment.getComments();
        Post replay=replays.get(repIndex);
        List<UserLoginResponse> list=replay.getDislikes();

        ///update
        list.add(UserLoginMapper.INSTANCE.toDto(user));
        replay.setDislikes(list);
        replays.remove(repIndex);
        replays.add(repIndex,replay);
        comment.setComments(replays);
        comments.remove(comIndex);
        comments.add(comIndex,comment);
        post.setComments(comments);
        return postRepository.save(post);
    }
}
