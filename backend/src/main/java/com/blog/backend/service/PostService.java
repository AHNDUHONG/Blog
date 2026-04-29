package com.blog.backend.service;


import com.blog.backend.domain.member.Member;
import com.blog.backend.domain.post.Post;
import com.blog.backend.dto.common.PageResponse;
import com.blog.backend.dto.post.PostCreateRequest;
import com.blog.backend.dto.post.PostListResponse;
import com.blog.backend.dto.post.PostResponse;
import com.blog.backend.dto.post.PostUpdateRequest;
import com.blog.backend.exception.BusinessException;
import com.blog.backend.exception.ErrorCode;
import com.blog.backend.repository.MemberRepository;
import com.blog.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createPost(Long memberId, PostCreateRequest request) {

        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.update(request.getTitle(), request.getContent());
    }

    @Transactional
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        postRepository.delete(post);
    }

    public PageResponse<PostListResponse> getPosts(String keyword, Pageable pageable) {

        Page<Post> posts;

        if (keyword == null || keyword.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContainingOrContentContaining(
                    keyword,
                    keyword,
                    pageable
            );
        }

        Page<PostListResponse> response = posts.map(PostListResponse::from);

        return PageResponse.from(response);
    }

    @Transactional
    public PostResponse getPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.increaseViewCount();

        return PostResponse.from(post);
    }
}
