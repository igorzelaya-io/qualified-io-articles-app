package com.example.articles.service.impl;

import com.example.articles.exception.NotFoundException;
import com.example.articles.model.Article;
import com.example.articles.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ArticleServiceImpl implements ArticleService {
    private List<Article> articles = new ArrayList<>();
    public ArticleServiceImpl() {

    }
    @Override
    public void setArticles(List<Article> articles) {
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

        Article modifiedArticle = findById(id)
                .toBuilder()
                .body(entity.getBody())
                .title(entity.getTitle())
                .type(entity.getArticleType())
                .comments(entity.getArticleComments())
                .build();

        IntStream
                .range(0, articles.size())
                .filter(index -> articles.get(index).getId() == id)
                .findFirst()
                .ifPresent(index -> articles.set(index, modifiedArticle));

        return modifiedArticle;
    }

    @Override
    public void delete(int id) {
        if (!articles.removeIf(article -> article.getId() == id)) {
            throw new NotFoundException(Article.class, "id", String.valueOf(id));
        }
    }
}
