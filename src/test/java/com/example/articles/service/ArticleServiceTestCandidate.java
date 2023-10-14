package com.example.articles.service;


import com.example.articles.constants.ArticleType;
import com.example.articles.exception.NotFoundException;
import com.example.articles.model.Article;
import com.example.articles.service.impl.ArticleServiceImpl;

import java.util.*;
import java.util.stream.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
public class ArticleServiceTestCandidate {

    private List<Article> articles;

    //@MockBean
    private ArticleService articleService;

    private Article article;

    private Article secondArticle;

    private static final int ARTICLE_ID = 1;

    private static final int INVALID_ID = 305;

    private static final String ARTICLE_TITLE = "Our road to Beating Qualified.io's assessment!";

    private static final String ARTICLE_BODY = "This is a buddy :)";

    @Before
    public void init(){
        article = new Article.Builder()
                .id(ARTICLE_ID)
                .title(ARTICLE_TITLE)
                .type(ArticleType.CASE_STUDY)
                .body(ARTICLE_BODY)
                .comments(new ArrayList<>())
                .build();

        secondArticle = new Article.Builder()
                .id(2)
                .title("How does persistence influence Human Life ?")
                .body("It might increase the probabilities of success over the long term.")
                .type(ArticleType.EMPIRICAL_STUDY)
                .comments(new ArrayList<>())
                .build();

        articleService = new ArticleServiceImpl();

        articles = Arrays.asList(article);

        articleService.setArticles(articles);

    }

    @Test
    public void shouldReturnArticlebyId() {

        Article foundArticle = articleService.findById(ARTICLE_ID);

        Assertions.assertNotNull(foundArticle);
        Assertions.assertEquals(foundArticle.getId(), ARTICLE_ID);
        Assertions.assertEquals(foundArticle.getTitle(), ARTICLE_TITLE);
        Assertions.assertEquals(foundArticle.getBody(), ARTICLE_BODY);
        Assertions.assertEquals(foundArticle.getArticleType(), article.getArticleType());

    }

    @Test
    public void whenNotFound_shouldThrowException(){

        Map<String, String> params = Map.of("id", String.valueOf(INVALID_ID));

        final String EX_MESSAGE = new StringBuilder("Article was not found for parameters")
                .append(" ")
                .append(params).toString();

        try {
            Article foundArticle = articleService.findById(INVALID_ID);
            Assertions.fail("Exception should've been thrown.");
        }
        catch(Exception ex) {
            Assertions.assertTrue(ex instanceof NotFoundException);
            Assertions.assertEquals(EX_MESSAGE, ex.getMessage());
        }
    }

    @Test
    public void shouldReturnAllArticles(){

        List<Article> foundArticles = articleService.findAll();

        Assertions.assertEquals(foundArticles.size(), articles.size());
        Assertions.assertEquals(foundArticles.get(0).getTitle(), articles.get(0).getTitle());
    }

    @Test
    public void shouldSaveArticle(){

        Article savedArticle = articleService.save(secondArticle);

        Assertions.assertNotNull(savedArticle);
        Assertions.assertEquals(savedArticle.getId(), 2);
        Assertions.assertEquals(savedArticle.getTitle(), "How does persistence influence Human Life ?");
        Assertions.assertEquals(savedArticle.getBody(), "It might increase the probabilities of success over the long term.");
        Assertions.assertEquals(savedArticle.getArticleType(), ArticleType.EMPIRICAL_STUDY);

    }

    @Test
    public void shouldDeleteArticle(){

        Map<String, String> params = Map.of("id", String.valueOf(ARTICLE_ID));

        final String EX_MESSAGE = new StringBuilder("Article was not found for parameters")
                .append(" ")
                .append(params).toString();

        articleService.delete(ARTICLE_ID);

        try{
            articleService.findById(ARTICLE_ID);
            Assertions.fail("Should've deleted the Article with given ID.");
        }
        catch(Exception e){
            Assertions.assertTrue(e instanceof NotFoundException);
            Assertions.assertEquals(EX_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void shouldUpdateArticle() {

        Article updatedArticle = articleService.update(ARTICLE_ID, secondArticle);

        try{

            int index = IntStream
                    .range(0, articles.size())
                    .filter(i -> articles.get(i).getId() == ARTICLE_ID)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Article not found at list"));

            Article supposedArticleAtIndex = articles.get(index);

            Assertions.assertNotNull(updatedArticle);
            Assertions.assertNotNull(supposedArticleAtIndex);
            Assertions.assertEquals(supposedArticleAtIndex.getTitle(), secondArticle.getTitle());
            Assertions.assertEquals(supposedArticleAtIndex.getArticleType(), secondArticle.getArticleType());
            Assertions.assertEquals(supposedArticleAtIndex.getBody(), secondArticle.getBody());

        }
        catch(Exception e){
            Assertions.fail("Article was not updated at list index.");
        }
    }
}
