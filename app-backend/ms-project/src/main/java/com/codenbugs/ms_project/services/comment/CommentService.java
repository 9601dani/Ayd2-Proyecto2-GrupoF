package com.codenbugs.ms_project.services.comment;

import com.codenbugs.ms_project.dtos.comment.CommentCreated;
import com.codenbugs.ms_project.dtos.comment.CommentResponse;
import com.codenbugs.ms_project.dtos.comment.NewCommentRequest;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.comment.CommentNotCreatedException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

import java.util.List;

public interface CommentService {


    CommentCreated saveComment(NewCommentRequest request) throws CommentNotCreatedException, CaseNotFoundException;
    List<CommentResponse> getCommentsByCaseId(Integer id, Integer idParent) throws UserNotFoundException;
}
