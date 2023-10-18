package com.example.articles.service.impl;

import com.example.articles.exception.NotFoundException;
import com.example.articles.model.Article;
import com.example.articles.model.Comment;
import com.example.articles.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ArticleServiceImpl implements ArticleService {
    private List<Article> articles = new ArrayList<>();
    public ArticleServiceImpl(List<Article> articles) {
        this.articles = articles;
    }
    @Override
    public List<Article> findAll() {
        return articles;
    }
    @Override
    public Article findById(int id) {
        return articles.stream()
                .filter(article -> article.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException
                        (Article.class, "id", String.valueOf(id)));
    }
    @Override
    public Article save(Article entity) {
        articles.add(entity);
        return entity;
    }
    @Override
    public Article update(int id, Article entity) {

        return findById(id)
                .toBuilder()
                .body(entity.getBody())
                .title(entity.getTitle())
                .type(entity.getArticleType())
                .build();

    }
    @Override
    public void delete(int id) {
        if (!articles.removeIf(article -> article.getId() == id)) {
            throw new NotFoundException(Article.class, "id", String.valueOf(id));
        }
    }
    @Override
    public void addCommentToArticle(int articleId, Comment commentToSave) {
        Article article = findById(articleId);
        List<Comment> comments = article.getArticleComments();

        boolean exists = comments.stream()
                .anyMatch(comment -> comment.getId() == commentToSave.getId());

        if(exists) {
            throw new IllegalArgumentException("Comment already exists.");
        }
        article.addComment(commentToSave);
    }
    @Override
    public void removeCommentFromArticle(int articleId, int commentId) {

        Article article = findById(articleId);
        List<Comment> articleComments = article.getArticleComments();

        boolean isRemoved = articleComments
                .removeIf(comment -> comment.getId() == commentId);

        if(!isRemoved){
            throw new NotFoundException(Comment.class, "id", String.valueOf(commentId));
        }
    }

}
