package com.blog.backend.dto.post;


import com.blog.backend.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostListResponse {

    private Long id;
    private String title;
    private int viewCount;
    private String authorNickname;
    private LocalDateTime createdAt;

    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .viewCount(post.getViewCount())
                .authorNickname(post.getAuthor().getNickname())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
