package com.blog.backend.controller;


import com.blog.backend.dto.comment.CommentCreateRequest;
import com.blog.backend.dto.comment.CommentResponse;
import com.blog.backend.dto.comment.CommentUpdateRequest;
import com.blog.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Long> createComment(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long commentId = commentService.createComment(
                Long.valueOf(memberId),
                postId,
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId
    ) {
        List<CommentResponse> response = commentService.getComments(postId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        commentService.updateComment(
                Long.valueOf(memberId),
                commentId,
                request
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal String memberId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(
                Long.valueOf(memberId),
                commentId
        );

        return ResponseEntity.noContent().build();
    }
}
