package com.hk.backendweb02.post.dto;

import com.hk.backendweb02.post.domain.Comment;
import lombok.Getter;
import lombok.Setter;

public class CommentDto {

    @Getter
    @Setter
    public static class Create {
        private String content;
        private Long userId;
        private Long postId;
    }

    @Getter
    @Setter
    public static class Update {
        private String content;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String content;
        private String authorUserId;
        private Long authorId;


        public Response(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            if (comment.getUser() != null) {
                this.authorUserId = comment.getUser().getUserId();
                this.authorId = comment.getUser().getId();
            }

        }
    }
}