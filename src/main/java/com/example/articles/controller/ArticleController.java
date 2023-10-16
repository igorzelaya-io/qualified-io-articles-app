package com.example.articles.controller;

import com.example.articles.dto.ArticleDto;
import com.example.articles.mapper.ArticleMapper;
import com.example.articles.model.Article;
import com.example.articles.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private ArticleMapper mapper;
    private ArticleService service;
    public ArticleController(ArticleService service){
        this.service = service;
        this.mapper = ArticleMapper.getInstance();
    }
    @GetMapping
    public ResponseEntity<List<? extends Article>> findAllArticles() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Article> findArticleById(@PathVariable("id") final int id) {
        Article article = service.findById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody @Valid final ArticleDto articleDto){
        Article article = service.save(mapper.fromDto(articleDto));
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") int id,
                                                 @RequestBody @Valid  ArticleDto articleDto) {
        Article updatedArticle = service.update(id, mapper.fromDto(articleDto));
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") int id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
