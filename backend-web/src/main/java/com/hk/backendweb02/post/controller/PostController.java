package com.hk.backendweb02.post.controller;

import com.hk.backendweb02.post.domain.Post;
import com.hk.backendweb02.post.dto.PostDto;
import com.hk.backendweb02.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto.Response> createPost(
            @RequestPart("postData") PostDto.Create requestDto,
            @RequestPart("imageFile") MultipartFile imageFile) {
        Post post = postService.createPost(requestDto, imageFile);
        return ResponseEntity.ok(new PostDto.Response(post, requestDto.getUserId()));
    }

    @GetMapping
    public ResponseEntity<Page<PostDto.Response>> getPosts(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Long currentUserId,
            @PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postPage = postService.findPosts(category, search, pageable);
        Page<PostDto.Response> responsePage = postPage.map(post -> new PostDto.Response(post, currentUserId));
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto.Response> getPostById(@PathVariable Long id, @RequestParam(required = false) Long currentUserId) {
        Post post = postService.findPostById(id);
        return ResponseEntity.ok(new PostDto.Response(post, currentUserId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestPart("postData") PostDto.Update requestDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) { // ✨ @RequestParam Long userId 제거
        try {
            Post updatedPost = postService.updatePost(id, requestDto.getUserId(), requestDto, imageFile);
            return ResponseEntity.ok(new PostDto.Response(updatedPost, requestDto.getUserId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        try {
            postService.deletePost(id, userId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/toggle-like")
    public ResponseEntity<String> toggleLike(@PathVariable Long id, @RequestParam Long userId) {
        postService.toggleLike(id, userId);
        return ResponseEntity.ok("좋아요 상태가 변경되었습니다.");
    }

    @GetMapping("/my-posts")
    public ResponseEntity<Page<PostDto.Response>> getMyPosts(
            @RequestParam Long userId,
            @PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postPage = postService.findPostsByUserId(userId, pageable);
        Page<PostDto.Response> responsePage = postPage.map(post -> new PostDto.Response(post, userId));
        return ResponseEntity.ok(responsePage);
    }
}