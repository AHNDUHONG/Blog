package com.blog.backend.controller;


import com.blog.backend.dto.common.PageResponse;
import com.blog.backend.dto.post.PostCreateRequest;
import com.blog.backend.dto.post.PostListResponse;
import com.blog.backend.dto.post.PostResponse;
import com.blog.backend.dto.post.PostUpdateRequest;
import com.blog.backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long postId = postService.createPost(Long.valueOf(memberId), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PostListResponse>> getPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        PageResponse<PostListResponse> response = postService.getPosts(keyword, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        postService.updatePost(postId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
