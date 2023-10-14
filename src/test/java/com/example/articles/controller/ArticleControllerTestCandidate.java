package com.example.articles.controller;

import com.example.articles.constants.ArticleType;
import com.example.articles.dto.ArticleDto;
import com.example.articles.model.Article;
import com.example.articles.service.ArticleService;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.junit.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
@ContextConfiguration(classes = ArticleController.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleControllerTestCandidate extends AbstractControllerTest{


    List<Article> articles;

    @MockBean
    private ArticleService articleService;

    private static final String baseUri = "/api/v1/articles";

    private Article article;

    private ArticleDto articleDto;

    private static final int ARTICLE_ID = 1;

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

        articleDto = new ArticleDto
                (ARTICLE_ID, ARTICLE_TITLE, ARTICLE_BODY, ArticleType.THEORIC, new ArrayList<>());

        articles = new ArrayList<>(Collections.singletonList(article));

    }

    @Test
    public void shouldRetrieveAllArticles() throws Exception {

        when(articleService.findAll()).thenReturn(articles);

        doRequestFindAllArticles()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(articles.size())));

    }
    private ResultActions doRequestFindAllArticles() throws Exception {
        return getMockMvc().perform(get(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }


    @Test
    public void shouldGetArticleById() throws Exception {

        when(articleService.findById(ARTICLE_ID))
                .thenReturn(article);

        doRequestGetArticleById()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ARTICLE_ID))
                .andExpect(jsonPath("$.title").value(ARTICLE_TITLE));
    }

    private ResultActions doRequestGetArticleById() throws Exception {
        return getMockMvc()
                .perform(get(baseUri + "/{id}", ARTICLE_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldSaveArticle() throws Exception {

        doRequestSaveArticle(articleDto)
                .andExpect(status().isCreated());
    }

    private ResultActions doRequestSaveArticle(final ArticleDto dto) throws Exception {
        return getMockMvc()
                .perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper()
                                .writeValueAsString(dto)));
    }

    @Test
    public void whenNullValue_thenReturnBadRequest() throws Exception {

        ArticleDto invalidDto = new ArticleDto
                (ARTICLE_ID, null, null, ArticleType.THEORIC, new ArrayList<>());

        doRequestSaveArticle(invalidDto)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldDeleteArticleById() throws Exception {

        doRequestDeleteArticle()
                .andExpect(status().isOk());
    }

    private ResultActions doRequestDeleteArticle() throws Exception {
        return getMockMvc()
                .perform(delete(baseUri + "/{id}", ARTICLE_ID)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldUpdateArticleById() throws Exception {

        Article updatedArticle = new Article.Builder()
                .id(1)
                .title("How does persistence influence Human Life ?")
                .body("It might increase the probabilities of success over the long term.")
                .type(ArticleType.EMPIRICAL_STUDY)
                .comments(new ArrayList<>())
                .build();

        doRequestUpdateArticle(articleDto)
                .andExpect(status().isOk());
    }

    @Test
    public void whenNullValueInUpdate_return404() throws Exception {

        ArticleDto invalidDto = new ArticleDto
                (ARTICLE_ID, null, null, ArticleType.THEORIC, new ArrayList<>());

        doRequestUpdateArticle(invalidDto)
                .andExpect(status().isBadRequest());
    }

    private ResultActions doRequestUpdateArticle(final ArticleDto dto) throws Exception {
        return getMockMvc()
                .perform(put(baseUri + "/{id}", ARTICLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)));

    }

}
