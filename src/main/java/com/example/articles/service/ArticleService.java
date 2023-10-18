package com.example.articles.service;

import com.example.articles.model.Article;
import com.example.articles.model.Comment;
import com.example.articles.repository.CrudRepository;

import java.util.List;

public interface ArticleService extends CrudRepository<Article> {

    @Override
    List<Article> findAll();

    @Override
    Article findById(int id);

    @Override
    Article save(Article entity);

    @Override
    Article update(int id, Article entity);

    @Override
    void delete(int id);

    void addCommentToArticle(final int articleId, Comment comment);

    void removeCommentFromArticle(final int articleId, final int commentId);

}
