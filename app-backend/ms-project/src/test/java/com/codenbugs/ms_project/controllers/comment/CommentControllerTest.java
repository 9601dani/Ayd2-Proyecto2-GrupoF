package com.codenbugs.ms_project.controllers.comment;

import com.codenbugs.ms_project.dtos.comment.CommentCreated;
import com.codenbugs.ms_project.dtos.comment.CommentResponse;
import com.codenbugs.ms_project.dtos.comment.NewCommentRequest;
import com.codenbugs.ms_project.services.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    private final Integer COMMENT_ID = 1;
    private final Integer CASE_ID = 2;
    private final Integer USER_ID = 3;
    private final Integer ID_PARENT = null;
    private final String CONTENT = "Comentario de prueba";
    private final String USERNAME = "erick";
    private final String PHOTO = "foto.jpg";
    private final LocalDateTime CREATED_AT = LocalDateTime.now();

    private CommentCreated commentCreated;
    private NewCommentRequest newCommentRequest;
    private CommentResponse commentResponse;

    @BeforeEach
    void setUp() {
        commentCreated = new CommentCreated(COMMENT_ID, CONTENT);
        newCommentRequest = new NewCommentRequest(CONTENT, USER_ID, CASE_ID, CREATED_AT, ID_PARENT);
        commentResponse = new CommentResponse(COMMENT_ID, CONTENT, CREATED_AT, USERNAME, PHOTO);
    }

    @Test
    void createComment() throws Exception {
        when(commentService.saveComment(any())).thenReturn(commentCreated);

        mockMvc.perform(post("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(commentCreated)));
    }

    @Test
    void findCommentsByCaseId() throws Exception {
        when(commentService.getCommentsByCaseId(CASE_ID, ID_PARENT)).thenReturn(List.of(commentResponse));

        mockMvc.perform(get("/v1/comments/find-by-case-id/{id}", CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(commentResponse))));
    }

    @Test
    void findCommentsByCaseIdWithParent() throws Exception {
        int parentId = 5;
        when(commentService.getCommentsByCaseId(CASE_ID, parentId)).thenReturn(List.of(commentResponse));

        mockMvc.perform(get("/v1/comments/find-by-case-id/{id}", CASE_ID)
                        .param("idParent", String.valueOf(parentId)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(commentResponse))));
    }
}