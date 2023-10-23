package com.example.articles.service;

import com.example.articles.model.Comment;
import com.example.articles.repository.CrudRepository;

import java.util.List;

public interface CommentService extends CrudRepository<Comment> {
    List<Comment> getArticleComments(final int articleId);
}
