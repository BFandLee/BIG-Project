package com.hk.backendweb02.post.repository;

import com.hk.backendweb02.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 기존 카테고리별 조회
    Page<Post> findByCategory(String category, Pageable pageable);

    // 카테고리 내에서 제목으로 검색하는 기능 추가
    Page<Post> findByCategoryAndTitleContaining(String category, String title, Pageable pageable);

    // 전체 게시글에서 제목으로 검색하는 기능
    Page<Post> findByTitleContaining(String title, Pageable pageable);

    // userId로 게시글 찾는 기능
    Page<Post> findByUserId(Long userId, Pageable pageable);

}