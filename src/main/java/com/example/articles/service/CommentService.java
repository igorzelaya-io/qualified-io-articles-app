package com.example.articles.service;

import com.example.articles.model.Comment;
import com.example.articles.repository.CrudRepository;

import java.util.List;

public interface CommentService extends CrudRepository<Comment> {

    @Override
    List<Comment> findAll();

    @Override
    Comment findById(int id);

    @Override
    Comment save(Comment entity);

    @Override
    Comment update(int id, Comment entity);

    @Override
    void delete(int id);

    public void setComments(List<Comment> comments);

}
