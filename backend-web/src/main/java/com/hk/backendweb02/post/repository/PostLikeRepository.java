package com.hk.backendweb02.post.repository;

import com.hk.backendweb02.post.domain.Post;
import com.hk.backendweb02.post.domain.PostLike;
import com.hk.backendweb02.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}