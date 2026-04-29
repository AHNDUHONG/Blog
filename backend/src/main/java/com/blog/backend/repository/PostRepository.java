package com.blog.backend.repository;

import com.blog.backend.domain.post.Category;
import com.blog.backend.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContainingOrContentContaining(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable
    );

    Page<Post> findByCategory(
            Category category,
            Pageable pageable
    );

    Page<Post> findByCategoryAndTitleContainingOrCategoryAndContentContaining(
            Category titleCategory,
            String titleKeyword,
            Category contentCategory,
            String contentKeyword,
            Pageable pageable
    );
}
