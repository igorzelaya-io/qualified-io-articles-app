package com.example.articles.controller;


import com.example.articles.dto.CommentDto;
import com.example.articles.mapper.CommentMapper;
import com.example.articles.model.Comment;
import com.example.articles.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private CommentMapper mapper;
    private CommentService service;

    public CommentController(CommentService commentService) {
        this.service = commentService;
        this.mapper = CommentMapper.getInstance();
    }

    @GetMapping
    public ResponseEntity<List<? extends Comment>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findById(@PathVariable("id") int id) {
        Comment comment = service.findById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comment> saveComment(@RequestBody @Valid CommentDto commentDto) {
        Comment savedComment = service.save(mapper.fromDto(commentDto));
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") int id,
                                                 @RequestBody @Valid CommentDto commentDto) {
        Comment updatedComment = service.update(id, mapper.fromDto(commentDto));
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("id") int id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
