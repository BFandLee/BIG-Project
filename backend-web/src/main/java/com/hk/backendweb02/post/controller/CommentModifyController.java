package com.hk.backendweb02.post.controller;

import com.hk.backendweb02.post.domain.Comment;
import com.hk.backendweb02.post.dto.CommentDto;
import com.hk.backendweb02.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentModifyController {

    private final CommentService commentService;

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestBody CommentDto.Update requestDto) {
        try {
            Comment updatedComment = commentService.updateComment(commentId, userId, requestDto);
            return ResponseEntity.ok(new CommentDto.Response(updatedComment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        try {
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}