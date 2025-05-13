package com.codenbugs.ms_project.services.comment;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.comment.CommentCreated;
import com.codenbugs.ms_project.dtos.comment.CommentResponse;
import com.codenbugs.ms_project.dtos.comment.NewCommentRequest;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.comment.CommentException;
import com.codenbugs.ms_project.exceptions.comment.CommentNotCreatedException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.comment.Comment;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.comment.CommentRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CaseRepository caseRepository;
    private final ProjectRepository projectRepository;
    private final UserRestClient userRestClient;

    @Override
    public CommentCreated saveComment(NewCommentRequest request) throws CommentNotCreatedException {

        boolean isCaseEnabled = this.caseRepository.existsByIsEnabled(true);
        boolean isProjectEnabled = this.projectRepository.existsByIsEnabled(true);

        if(!isCaseEnabled){
            throw new CommentNotCreatedException("El caso no se encuentra habilitado.");
        }

        if(!isProjectEnabled){
            throw new CommentNotCreatedException("El projecto no se encuentra habilitado.");
        }

        Comment comment = new Comment();
        comment.setCreatedDate(request.createdAt());
        comment.setContent(request.content());
        comment.setFkCase(request.idCase());
        comment.setFkUser(request.idUser());
        comment.setIdParent(request.idParent());
        comment = commentRepository.save(comment);
        return new CommentCreated(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByCaseId(Integer id, Integer idParent) throws UserNotFoundException {
        List<Comment> comments = commentRepository.findByFkCaseAndIdParentOrderByCreatedDateDesc(id, idParent);
        List<CommentResponse> response = new ArrayList<>();
        for (Comment comment : comments) {
            UserResponse user = this.userRestClient.findById(comment.getFkUser());
            CommentResponse commentResponse = new CommentResponse(comment, user);
            response.add(commentResponse);
        }

        return response;
    }
}
