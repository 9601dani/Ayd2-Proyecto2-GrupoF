package com.codenbugs.ms_project.dtos.comment;


import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.model.comment.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Integer id, String content, LocalDateTime createdAt, String username, String photo) {

    public CommentResponse(Comment comment, UserResponse userResponse) {
        this(comment.getId(), comment.getContent(), comment.getCreatedDate(), userResponse.username(), userResponse.photo());
    }

}
