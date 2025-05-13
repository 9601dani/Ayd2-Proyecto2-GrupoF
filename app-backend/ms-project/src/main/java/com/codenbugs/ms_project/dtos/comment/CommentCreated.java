package com.codenbugs.ms_project.dtos.comment;

import com.codenbugs.ms_project.model.comment.Comment;

public record CommentCreated(Integer id, String content) {

    public CommentCreated(Comment comment) {
        this(comment.getId(), comment.getContent());
    }
}
