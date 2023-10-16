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

    public CommentServiceImpl(List<Comment> comments){
        this.comments = comments;
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(comments);
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

        return findById(id)
                .toBuilder()
                .text(entity.getText())
                .email(entity.getEmail())
                .updatedAt(LocalDateTime.now())
                .id_article(entity.getId_article())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    @Override
    public void delete(int id) {
        if(!comments.removeIf(comment -> comment.getId() == id)){
            throw new NotFoundException(Comment.class, "id", String.valueOf(id));
        }
    }

}
