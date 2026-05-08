package com.blog.backend.service;


import com.blog.backend.domain.comment.Comment;
import com.blog.backend.domain.member.Member;
import com.blog.backend.domain.member.Role;
import com.blog.backend.domain.post.Post;
import com.blog.backend.dto.comment.CommentCreateRequest;
import com.blog.backend.dto.comment.CommentResponse;
import com.blog.backend.dto.comment.CommentUpdateRequest;
import com.blog.backend.exception.BusinessException;
import com.blog.backend.exception.ErrorCode;
import com.blog.backend.repository.CommentRepository;
import com.blog.backend.repository.MemberRepository;
import com.blog.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createComment(Long memberId, Long postId, CommentCreateRequest request) {

        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }

    public List<CommentResponse> getComments(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void updateComment(Long memberId, Long commentId, CommentUpdateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        validateCommentOwnerOrAdmin(member, comment);

        comment.update(request.getContent());
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        validateCommentOwnerOrAdmin(member, comment);

        commentRepository.delete(comment);
    }

    private void validateCommentOwnerOrAdmin(Member member, Comment comment) {

        boolean isOwner = comment.getAuthor().getId().equals(member.getId());
        boolean isAdmin = member.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new BusinessException(ErrorCode.FORBIDDEN_COMMENT_ACCESS);
        }
    }
}
