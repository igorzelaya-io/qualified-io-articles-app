package com.example.articles.controller;


import com.example.articles.dto.CommentDto;
import com.example.articles.mapper.CommentMapper;
import com.example.articles.model.Comment;
import com.example.articles.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentMapper mapper;
    private final CommentService service;
    public CommentController(CommentService commentService) {
        this.service = commentService;
        this.mapper = CommentMapper.getInstance();
    }

    @GetMapping("/comments")
    public ResponseEntity<List<? extends Comment>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/comments", params = {"startDate", "endDate"})
    public ResponseEntity<List<Comment>> findAllCreatedInBetweenDate
            (@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
             @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Comment> comments = service.findCreatedInBetweenDate(startDate, endDate);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> findById(@PathVariable("id") int id) {
        Comment comment = service.findById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/comments/emails")
    public ResponseEntity<Comment> findCommentByEmail(@RequestParam @Email final String email) {
        Comment comment = service.findCommentByEmail(email);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> saveComment(@RequestBody @Valid CommentDto commentDto) {
        Comment savedComment = service.save(mapper.fromDto(commentDto));
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") int id,
                                                 @RequestBody @Valid CommentDto commentDto) {
        Comment updatedComment = service.update(id, mapper.fromDto(commentDto));
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("id") int id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<Comment>> findAllCommentsByArticleId(@PathVariable("articleId") final Integer articleId) {
        List<Comment> comments = service.getArticleComments(articleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
