package com.hk.backendweb02.post.dto;

import com.hk.backendweb02.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

public class PostDto {
    @Getter
    @Setter
    public static class Create {
        private String title;
        private String content;
        private String category;
        private String ingredients;
        private Long userId;
    }

    @Getter
    @Setter
    public static class Update {
        private String title;
        private String content;
        private String category;
        private String ingredients;
        private Long userId;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String title;
        private String imageUrl;
        private String category;
        private String ingredients;
        private String content;
        private String authorName;
        private Long authorId;
        private String authorUserId;
        private long likeCount;
        private boolean likedByCurrentUser;

        public Response(Post post, Long currentUserId) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.imageUrl = post.getImageUrl();
            this.category = post.getCategory();
            this.ingredients = post.getIngredients();
            this.content = post.getContent();
            if (post.getUser() != null) {
                this.authorName = post.getUser().getName();
                this.authorId = post.getUser().getId();
                this.authorUserId = post.getUser().getUserId();
            }
            this.likeCount = post.getLikes().size();
            if (currentUserId != null) {
                this.likedByCurrentUser = post.getLikes().stream()
                        .anyMatch(like -> like.getUser().getId().equals(currentUserId));
            } else {
                this.likedByCurrentUser = false;
            }
        }
    }
}