package com.example.articles.service.impl;

import com.example.articles.exception.NotFoundException;
import com.example.articles.model.Comment;
import com.example.articles.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CommentServiceImpl implements CommentService {

    private List<Comment> comments = new ArrayList<>();

    public CommentServiceImpl(){

    }

    @Override
    public void setComments(List<Comment> comments){
        this.comments = comments;
    }

    @Override
    public List<Comment> findAll() {
        return this.comments;
    }

    @Override
    public Comment findById(int id) {
        return comments
                .stream()
                .filter(comment -> comment.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException
                        (Comment.class, "id", String.valueOf(id)));
    }

    @Override
    public Comment save(Comment entity) {
        comments.add(entity);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    @Override
    public Comment update(int id, Comment entity) {

        Comment modifiedComment = findById(id)
                .toBuilder()
                .text(entity.getText())
                .email(entity.getEmail())
                .updatedAt(LocalDateTime.now())
                .id_article(entity.getId_article())
                .createdAt(entity.getCreatedAt())
                .build();

        IntStream
                .range(0, comments.size())
                .filter(index -> comments.get(index).getId() == id)
                .findFirst()
                .ifPresent(index -> comments.set(index, modifiedComment));

        return modifiedComment;
    }

    @Override
    public void delete(int id) {
        if(!comments.removeIf(comment -> comment.getId() == id)){
            new NotFoundException(Comment.class, "id", String.valueOf(id));
        }
    }

}
