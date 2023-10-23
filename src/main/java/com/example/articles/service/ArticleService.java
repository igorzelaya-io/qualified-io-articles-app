package com.example.articles.service;

import com.example.articles.model.Article;
import com.example.articles.model.Comment;
import com.example.articles.repository.CrudRepository;

import java.util.List;

public interface ArticleService extends CrudRepository<Article> {
    void addCommentToArticle(final int articleId, Comment comment);

    void removeCommentFromArticle(final int articleId, final int commentId);

}
