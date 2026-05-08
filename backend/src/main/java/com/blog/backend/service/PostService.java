package com.blog.backend.service;


import com.blog.backend.domain.member.Member;
import com.blog.backend.domain.post.Category;
import com.blog.backend.domain.post.Post;
import com.blog.backend.dto.common.PageResponse;
import com.blog.backend.dto.post.PostCreateRequest;
import com.blog.backend.dto.post.PostListResponse;
import com.blog.backend.dto.post.PostResponse;
import com.blog.backend.dto.post.PostUpdateRequest;
import com.blog.backend.exception.BusinessException;
import com.blog.backend.exception.ErrorCode;
import com.blog.backend.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    @Transactional
    public Long createPost(Long memberId, PostCreateRequest request) {

        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .author(author)
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getCategory()
        );
    }

    @Transactional
    public void deletePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        commentRepository.deleteByPostId(postId);

        postRepository.delete(post);
    }

    public PageResponse<PostListResponse> getPosts(
            Category category,
            String keyword,
            Pageable pageable
    ) {
        Page<Post> posts;

        boolean hasCategory = category !=null;
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (hasCategory && hasKeyword) {
            posts = postRepository.findByCategoryAndTitleContainingOrCategoryAndContentContaining(
                    category,
                    keyword,
                    category,
                    keyword,
                    pageable
            );
        } else if (hasCategory) {
            posts = postRepository.findByCategory(category, pageable);
        } else if (hasKeyword) {
            posts = postRepository.findByTitleContainingOrContentContaining(
                    keyword,
                    keyword,
                    pageable
            );
        } else {
            posts = postRepository.findAll(pageable);
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
