package com.blog.backend.dto.post;

import com.blog.backend.domain.post.Category;
import com.blog.backend.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Category category;
    private int viewCount;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .viewCount(post.getViewCount())
                .authorNickname(post.getAuthor().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
