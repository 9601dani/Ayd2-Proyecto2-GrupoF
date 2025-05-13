package com.codenbugs.ms_project.dtos.comment;

import java.time.LocalDateTime;

public record NewCommentRequest(String content, Integer idUser, Integer idCase, LocalDateTime createdAt, Integer idParent) {
}
