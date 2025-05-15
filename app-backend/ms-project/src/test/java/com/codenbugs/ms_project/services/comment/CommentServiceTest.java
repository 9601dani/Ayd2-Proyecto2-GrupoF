package com.codenbugs.ms_project.services.comment;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.comment.CommentCreated;
import com.codenbugs.ms_project.dtos.comment.CommentResponse;
import com.codenbugs.ms_project.dtos.comment.NewCommentRequest;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.comment.CommentException;
import com.codenbugs.ms_project.exceptions.comment.CommentNotCreatedException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.comment.Comment;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.comment.CommentRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRestClient userRestClient;

    @InjectMocks
    private CommentServiceImpl commentService;

    private final String CONTENT = "content";
    private final Integer IDUSER = 1;
    private final Integer IDPROJECT = 2;
    private final Integer IDCASE = 2;
    private final LocalDateTime CREATEDAT = LocalDateTime.now();
    private final Integer IDPARENT = 1;
    private final Integer IDCOMMENT = 2;

    private final String USERNAME ="username";
    private final Integer ROLE = 1;
    private final String PHOTO = "photo";
    private final BigDecimal SALARY = new BigDecimal("100");
    private final Boolean ENABLED = true;


    @Test
    public void saveComment_shouldReturnCreatedComment() throws CommentNotCreatedException {
        // Arrange
        NewCommentRequest request = new NewCommentRequest(
                CONTENT,
                IDUSER,
                IDCASE,
                CREATEDAT,
                IDPARENT
        );

        Comment savedComment = new Comment();
        savedComment.setId(IDCOMMENT);
        savedComment.setFkCase(request.idCase());
        savedComment.setFkUser(request.idUser());
        savedComment.setContent(request.content());
        savedComment.setIdParent(request.idParent());
        savedComment.setCreatedDate(request.createdAt());

        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // Act
        CommentCreated result = commentService.saveComment(request);

        // Assert
        assertNotNull(result);
        assertEquals(IDCOMMENT, result.id());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void getCommentsByCaseId_shouldReturnListOfCommentResponse() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setId(IDCOMMENT);
        comment.setFkCase(IDCASE);
        comment.setFkUser(IDUSER);
        comment.setContent(CONTENT);
        comment.setCreatedDate(CREATEDAT);
        comment.setIdParent(IDPARENT);

        UserResponse user = new UserResponse(IDUSER,USERNAME,ROLE,PHOTO,SALARY,ENABLED);

        when(commentRepository.findByFkCaseAndIdParentOrderByCreatedDateDesc(IDCASE, IDPARENT))
                .thenReturn(List.of(comment));
        when(userRestClient.findById(IDUSER)).thenReturn(user);

        // Act
        List<CommentResponse> responses = commentService.getCommentsByCaseId(IDCASE, IDPARENT);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(CONTENT, responses.get(0).content());
        assertEquals(USERNAME, responses.get(0).username());

        verify(commentRepository).findByFkCaseAndIdParentOrderByCreatedDateDesc(IDCASE, IDPARENT);
        verify(userRestClient).findById(IDUSER);
    }

    @Test
    public void getCommentsByCaseId_shouldThrowCommentException_whenUserRestClientFails() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setId(IDCOMMENT);
        comment.setFkCase(IDCASE);
        comment.setFkUser(IDUSER);
        comment.setContent(CONTENT);
        comment.setCreatedDate(CREATEDAT);
        comment.setIdParent(IDPARENT);

        when(commentRepository.findByFkCaseAndIdParentOrderByCreatedDateDesc(IDCASE, IDPARENT))
                .thenReturn(List.of(comment));

        when(userRestClient.findById(IDUSER)).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            commentService.getCommentsByCaseId(IDCASE, IDPARENT);
        });
    }

    @Test
    public void saveComment_shouldThrowException_whenRepositoryFails() {
        // Arrange
        NewCommentRequest request = new NewCommentRequest(CONTENT, IDUSER, IDCASE, CREATEDAT, IDPARENT);

        when(commentRepository.save(any(Comment.class)))
                .thenThrow(new RuntimeException("Simulated database error"));

        // Act & Assert
        assertThrows(CommentNotCreatedException.class, () -> {
            commentService.saveComment(request);
        });
    }
}
