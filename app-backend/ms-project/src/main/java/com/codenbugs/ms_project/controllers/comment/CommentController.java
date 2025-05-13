package com.codenbugs.ms_project.controllers.comment;

import com.codenbugs.ms_project.dtos.comment.CommentCreated;
import com.codenbugs.ms_project.dtos.comment.CommentResponse;
import com.codenbugs.ms_project.dtos.comment.NewCommentRequest;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.services.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentCreated> createComment(@RequestBody NewCommentRequest request) {
        CommentCreated comment = this.commentService.saveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/find-by-case-id/{id}")
    public ResponseEntity<List<CommentResponse>> findCommentsByCaseId(
            @PathVariable Integer id,
            @RequestParam(value = "idParent", required = false) Integer idParent) throws UserNotFoundException {
        List<CommentResponse> response = this.commentService.getCommentsByCaseId(id, idParent);
        return ResponseEntity.ok(response);
    }
}
