package com.hk.backendweb02.post.service;

import com.hk.backendweb02.post.domain.Comment;
import com.hk.backendweb02.post.domain.Post;
import com.hk.backendweb02.post.dto.CommentDto;
import com.hk.backendweb02.post.repository.CommentRepository;
import com.hk.backendweb02.post.repository.PostRepository;
import com.hk.backendweb02.user.domain.User;
import com.hk.backendweb02.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment createComment(CommentDto.Create requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(requestDto.getContent());

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> findComments(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDto.Response::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Comment updateComment(Long commentId, Long userId, CommentDto.Update requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("댓글을 수정할 권한이 없습니다.");
        }
        comment.setContent(requestDto.getContent());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }
}