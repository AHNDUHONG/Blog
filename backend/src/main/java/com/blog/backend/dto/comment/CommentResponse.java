package com.blog.backend.dto.comment;

import com.blog.backend.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorNickname(comment.getAuthor().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
