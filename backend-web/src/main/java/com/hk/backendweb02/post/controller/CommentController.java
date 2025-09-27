package com.hk.backendweb02.post.controller;

import com.hk.backendweb02.post.domain.Comment;
import com.hk.backendweb02.post.dto.CommentDto;
import com.hk.backendweb02.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentDto.Create requestDto) {
        requestDto.setPostId(postId); // URL의 postId를 DTO에 설정
        Comment newComment = commentService.createComment(requestDto);
        return ResponseEntity.ok(new CommentDto.Response(newComment));
    }

    @GetMapping
    public ResponseEntity<List<CommentDto.Response>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findComments(postId));
    }
}