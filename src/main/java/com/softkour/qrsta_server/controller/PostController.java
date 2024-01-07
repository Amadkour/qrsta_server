package com.softkour.qrsta_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.Post;
import com.softkour.qrsta_server.payload.request.PostRequest;
import com.softkour.qrsta_server.service.PostService;
import com.softkour.qrsta_server.service.SessionService;

@RestController
@RequestMapping("/api/post/")
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    SessionService sessionService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> addPost(@RequestBody PostRequest postRequest) {

        Post post = new Post();
        post.setData(postRequest.getData());
        post.setMedia(postRequest.getMedia());
        if (postRequest.getSessionId() != null)
            post.setSessionId(postRequest.getSessionId());
        /// add replay
        System.out.println("================================================================");
        System.out.println(postRequest.toString());
        if (postRequest.getCommentIndex() != null && postRequest.getParentId() != null)
            return GenericResponse
                    .success(postService.addReplay(post, postRequest.getParentId(), postRequest.getCommentIndex()));
        /// add comment
        else if (postRequest.getParentId() != null)
            return GenericResponse.success(postService.addComment(post, postRequest.getParentId()));
        else {
            /// create post
            post.setCourseId(postRequest.getCourseId());

            return GenericResponse.success(postService.addPost(post));
        }

    }

    // @PostMapping("add_like")
    // public ResponseEntity<GenericResponse<Object>> addLike(
    // @RequestHeader String postIndex,
    // @RequestHeader String commentIndex,
    // @RequestHeader String replayIndex
    // ) {
    // try {
    //
    // ///replay
    // if (replayIndex != null)
    // return GenericResponse.success(postService.addLikeToReplay(replayIndex,
    // commentIndex, postIndex));
    // /// comment
    // else if (commentIndex != null)
    // return GenericResponse.success(postService.addLikeToComment(commentIndex,
    // postIndex));
    // else
    // /// post
    // return GenericResponse.success(postService.addLikeToPost(postIndex));
    //
    // } catch (Exception e) {
    // return GenericResponse.errorOfException(e);
    //
    // }
    // }
    //
    // @PostMapping("remove_like")
    // public ResponseEntity<GenericResponse<Object>> removeLike(
    // @RequestHeader String postIndex,
    // @RequestHeader String commentIndex,
    // @RequestHeader String replayIndex
    // ) {
    // try {
    //
    // ///replay
    // if (replayIndex != null)
    // return GenericResponse.success(postService.addLikeToReplay(replayIndex,
    // commentIndex, postIndex));
    // /// comment
    // else if (commentIndex != null)
    // return
    // GenericResponse.success(postService.removeLikeFromComment(commentIndex,
    // postIndex));
    // else
    // /// post
    // return GenericResponse.success(postService.removeLikeFromPost(postIndex));
    //
    // } catch (Exception e) {
    // return GenericResponse.errorOfException(e);
    //
    // }
    // }

    @PostMapping("like")
    public ResponseEntity<GenericResponse<Object>> like(
            @RequestHeader String postId,
            @RequestHeader(required = false) String commentIndex,
            @RequestHeader(required = false) String replayIndex,
            @RequestHeader boolean newValue

    ) {

        /// replay
        if (replayIndex != null)
            return GenericResponse
                    .success(postService.updateLikeToReplay(replayIndex, commentIndex, postId, newValue));
        /// comment
        else if (commentIndex != null)
            return GenericResponse.success(postService.updateCommentLike(commentIndex, postId, newValue));
        else
            /// postIndex
            return GenericResponse.success(postService.updatePostLike(postId, newValue));

    }

    //
    // @PostMapping("add_dislike")
    // public ResponseEntity<GenericResponse<Object>> addDislike(
    // @RequestHeader String postIndex,
    // @RequestHeader String commentIndex,
    // @RequestHeader String replayIndex
    // ) {
    // try {
    //
    // ///replay
    // if (replayIndex != null)
    // return GenericResponse.success(postService.addDislikeToReplay(replayIndex,
    // commentIndex, postIndex));
    // /// comment
    // else if (commentIndex != null)
    // return GenericResponse.success(postService.addDislikeToComment(commentIndex,
    // postIndex));
    // else
    // /// post
    // return GenericResponse.success(postService.addDislikeToPost(postIndex));
    //
    // } catch (Exception e) {
    // return GenericResponse.errorOfException(e);
    //
    // }
    // }
    //
    // @PostMapping("remove_dislike")
    // public ResponseEntity<GenericResponse<Object>> removeDislike(
    // @RequestHeader String postIndex,
    // @RequestHeader String commentIndex,
    // @RequestHeader String replayIndex
    // ) {
    // try {
    //
    // ///replay
    // if (replayIndex != null)
    // return GenericResponse.success(postService.addDislikeToReplay(replayIndex,
    // commentIndex, postIndex));
    // /// comment
    // else if (commentIndex != null)
    // return
    // GenericResponse.success(postService.removeDislikeFromComment(commentIndex,
    // postIndex));
    // else
    // /// post
    // return GenericResponse.success(postService.removeDislikeFromPost(postIndex));
    //
    // } catch (Exception e) {
    // return GenericResponse.errorOfException(e);
    //
    // }
    // }

    @PostMapping("dislike")
    public ResponseEntity<GenericResponse<Object>> dislike(
            @RequestHeader String postId,
            @RequestHeader(required = false) String commentIndex,
            @RequestHeader(required = false) String replayIndex,
            @RequestHeader boolean newValue

    ) {
        try {
            /// replay
            if (replayIndex != null)
                return GenericResponse
                        .success(postService.updateDislikeToReplay(replayIndex, commentIndex, postId, newValue));
            /// comment
            else if (commentIndex != null)
                return GenericResponse.success(postService.updateCommentDislike(commentIndex, postId, newValue));
            else
                /// post
                return GenericResponse.success(postService.updatePostDislike(postId, newValue));
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);

        }
    }

    @PostMapping("link_session")
    public ResponseEntity<GenericResponse<Object>> linkSession(
            @RequestHeader String postId,
            @RequestHeader(required = false) String commentIndex,
            @RequestHeader(required = false) String replayIndex,
            @RequestHeader Long sessionId) {
        try {

            /// replay
            if (replayIndex != null)
                return GenericResponse
                        .success(postService.linkSessionToReplay(replayIndex, commentIndex, postIndex, sessionId));
            /// comment
            else if (commentIndex != null)
                return GenericResponse.success(postService.linkSessionToComment(commentIndex, postIndex, sessionId));
            else
                /// post
                return GenericResponse.success(postService.linkSessionToPost(postIndex, sessionId));

        } catch (Exception e) {
            return GenericResponse.errorOfException(e);

        }
    }

}
