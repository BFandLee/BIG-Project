package com.hk.backendweb02.post.service;

import com.hk.backendweb02.post.domain.Post;
import com.hk.backendweb02.post.domain.PostLike;
import com.hk.backendweb02.post.dto.PostDto;
import com.hk.backendweb02.post.repository.PostLikeRepository;
import com.hk.backendweb02.post.repository.PostRepository;
import com.hk.backendweb02.user.domain.User;
import com.hk.backendweb02.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public Post createPost(PostDto.Create requestDto, MultipartFile imageFile) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        String imageUrl = fileStorageService.storeFile(imageFile);
        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setCategory(requestDto.getCategory());
        post.setIngredients(requestDto.getIngredients());
        post.setImageUrl(imageUrl);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<Post> findPosts(String category, String title, Pageable pageable) {
        boolean hasSearchTerm = title != null && !title.isBlank();
        if ("all".equals(category)) {
            return hasSearchTerm
                    ? postRepository.findByTitleContaining(title, pageable)
                    : postRepository.findAll(pageable);
        } else {
            return hasSearchTerm
                    ? postRepository.findByCategoryAndTitleContaining(category, title, pageable)
                    : postRepository.findByCategory(category, pageable);
        }
    }

    @Transactional(readOnly = true)
    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    @Transactional
    public Post updatePost(Long postId, Long userId, PostDto.Update requestDto, MultipartFile newImageFile) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setCategory(requestDto.getCategory());
        post.setIngredients(requestDto.getIngredients());
        if (newImageFile != null && !newImageFile.isEmpty()) {
            String newImageUrl = fileStorageService.storeFile(newImageFile);
            post.setImageUrl(newImageUrl);
        }
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

    @Transactional
    public void toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);
        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        } else {
            PostLike newLike = new PostLike();
            newLike.setUser(user);
            newLike.setPost(post);
            postLikeRepository.save(newLike);
        }
    }

    @Transactional(readOnly = true)
    public Page<Post> findPostsByUserId(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable);
    }
}