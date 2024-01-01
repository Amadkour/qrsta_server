package com.softkour.qrsta_server.controller;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Post;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.payload.request.PostRequest;
import com.softkour.qrsta_server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/post/")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> addPost(@RequestBody PostRequest postRequest) {
        try {
            Post post = new Post();
            post.setData(postRequest.getData());
            post.setMedia(postRequest.getMedia());
            post.setDate(Instant.now());
            ///add replay
            if (postRequest.getCommentIndex() != null)
                return GenericResponse.success(postService.addReplay(post, postRequest.getParentId(), postRequest.getCommentIndex()));
                ///add comment
            else if (postRequest.getParentId() != null)
                return GenericResponse.success(postService.addComment(post, postRequest.getParentId()));
            else
                ///create post
                return GenericResponse.success(postService.addPost(post));

        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());

        }
    }

//    @PostMapping("add_like")
//    public ResponseEntity<GenericResponse<Object>> addLike(
//            @RequestHeader String postIndex,
//            @RequestHeader String commentIndex,
//            @RequestHeader String replayIndex
//    ) {
//        try {
//
//            ///replay
//            if (replayIndex != null)
//                return GenericResponse.success(postService.addLikeToReplay(replayIndex, commentIndex, postIndex));
//                /// comment
//            else if (commentIndex != null)
//                return GenericResponse.success(postService.addLikeToComment(commentIndex, postIndex));
//            else
//                /// post
//                return GenericResponse.success(postService.addLikeToPost(postIndex));
//
//        } catch (Exception e) {
//            return GenericResponse.error(e.getMessage());
//
//        }
//    }
//
//    @PostMapping("remove_like")
//    public ResponseEntity<GenericResponse<Object>> removeLike(
//            @RequestHeader String postIndex,
//            @RequestHeader String commentIndex,
//            @RequestHeader String replayIndex
//    ) {
//        try {
//
//            ///replay
//            if (replayIndex != null)
//                return GenericResponse.success(postService.addLikeToReplay(replayIndex, commentIndex, postIndex));
//                /// comment
//            else if (commentIndex != null)
//                return GenericResponse.success(postService.removeLikeFromComment(commentIndex, postIndex));
//            else
//                /// post
//                return GenericResponse.success(postService.removeLikeFromPost(postIndex));
//
//        } catch (Exception e) {
//            return GenericResponse.error(e.getMessage());
//
//        }
//    }

    @PostMapping("like")
    public ResponseEntity<GenericResponse<Object>> like(
            @RequestHeader String postIndex,
            @RequestHeader String commentIndex,
            @RequestHeader String replayIndex,
            @RequestHeader boolean newValue

    ) {
        try {
            ///replay
            if (replayIndex != null)
                return GenericResponse.success(postService.updateLikeToReplay(replayIndex, commentIndex, postIndex, newValue));
                /// comment
            else if (commentIndex != null)
                return GenericResponse.success(postService.updateCommentLike(commentIndex, postIndex, newValue));
            else
                /// post
                return GenericResponse.success(postService.updatePostLike(postIndex, newValue));

        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());

        }
    }

//
//    @PostMapping("add_dislike")
//    public ResponseEntity<GenericResponse<Object>> addDislike(
//            @RequestHeader String postIndex,
//            @RequestHeader String commentIndex,
//            @RequestHeader String replayIndex
//    ) {
//        try {
//
//            ///replay
//            if (replayIndex != null)
//                return GenericResponse.success(postService.addDislikeToReplay(replayIndex, commentIndex, postIndex));
//                /// comment
//            else if (commentIndex != null)
//                return GenericResponse.success(postService.addDislikeToComment(commentIndex, postIndex));
//            else
//                /// post
//                return GenericResponse.success(postService.addDislikeToPost(postIndex));
//
//        } catch (Exception e) {
//            return GenericResponse.error(e.getMessage());
//
//        }
//    }
//
//    @PostMapping("remove_dislike")
//    public ResponseEntity<GenericResponse<Object>> removeDislike(
//            @RequestHeader String postIndex,
//            @RequestHeader String commentIndex,
//            @RequestHeader String replayIndex
//    ) {
//        try {
//
//            ///replay
//            if (replayIndex != null)
//                return GenericResponse.success(postService.addDislikeToReplay(replayIndex, commentIndex, postIndex));
//                /// comment
//            else if (commentIndex != null)
//                return GenericResponse.success(postService.removeDislikeFromComment(commentIndex, postIndex));
//            else
//                /// post
//                return GenericResponse.success(postService.removeDislikeFromPost(postIndex));
//
//        } catch (Exception e) {
//            return GenericResponse.error(e.getMessage());
//
//        }
//    }

    @PostMapping("dislike")
    public ResponseEntity<GenericResponse<Object>> dislike(
            @RequestHeader String postIndex,
            @RequestHeader String commentIndex,
            @RequestHeader String replayIndex,
            @RequestHeader boolean newValue

    ) {
        try {
            ///replay
            if (replayIndex != null)
                return GenericResponse.success(postService.updateDislikeToReplay(replayIndex, commentIndex, postIndex, newValue));
                /// comment
            else if (commentIndex != null)
                return GenericResponse.success(postService.updateCommentDislike(commentIndex, postIndex, newValue));
            else
                /// post
                return GenericResponse.success(postService.updatePostDislike(postIndex, newValue));
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());

        }
    }

    @PostMapping("link_session")
    public ResponseEntity<GenericResponse<Object>> linkSession(
            @RequestHeader String postIndex,
            @RequestHeader String commentIndex,
            @RequestHeader String replayIndex,
            @RequestHeader Long sessionId
    ) {
        try {

            ///replay
            if (replayIndex != null)
                return GenericResponse.success(postService.linkSessionToReplay(replayIndex, commentIndex, postIndex, sessionId));
                /// comment
            else if (commentIndex != null)
                return GenericResponse.success(postService.linkSessionToComment(commentIndex, postIndex, sessionId));
            else
                /// post
                return GenericResponse.success(postService.linkSessionToPost(postIndex, sessionId));

        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());

        }
    }


}
