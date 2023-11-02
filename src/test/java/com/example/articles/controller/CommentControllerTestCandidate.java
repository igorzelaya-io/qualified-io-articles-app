package com.example.articles.controller;

import com.example.articles.dto.CommentDto;
import com.example.articles.model.Article;
import com.example.articles.model.Comment;
import com.example.articles.service.CommentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
@ContextConfiguration(classes = CommentController.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentControllerTestCandidate extends AbstractControllerTest {

    private List<Comment> comments;

    @MockBean
    private CommentService commentService;
    private static final String baseUri = "/api/v1/comments";

    private Comment comment;

    private CommentDto commentDto;

    private static final int COMMENT_ID = 1;

    private static final int ARTICLE_ID = 1;

    private static final String COMMENT_TEXT = "This is a great test!";

    private static final String COMMENT_EMAIL = "test@test.com";

    @Before
    public void init(){

        comment = new Comment.Builder()
                .id(COMMENT_ID)
                .text(COMMENT_TEXT)
                .email(COMMENT_EMAIL)
                .id_article(null)
                .createdAt(LocalDateTime.now())
                .build();

        commentDto = new CommentDto(COMMENT_ID, COMMENT_EMAIL, COMMENT_TEXT, new Article());

        comments = new ArrayList<>(Arrays.asList(comment));

    }

    @Test
    public void shouldRetrieveAllCommments() throws Exception {

        when(commentService
                .findAll()).thenReturn(comments);

        doRequestFindAllComments().andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(comments.size())));
    }

    private ResultActions doRequestFindAllComments() throws Exception {
        return getMockMvc().perform(get(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldGetCommentById() throws Exception {

        when(commentService.findById(COMMENT_ID))
                .thenReturn(comment);

        doRequestGetCommentById()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.email").value(COMMENT_EMAIL))
                .andExpect(jsonPath("$.text").value(COMMENT_TEXT));
    }

    private ResultActions doRequestGetCommentById() throws Exception {
        return getMockMvc()
                .perform(get(baseUri + "/{id}", COMMENT_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void shouldSaveComment() throws Exception {

        when(commentService
                .save(comment)).thenReturn(comment);

        doRequestSaveComment(commentDto)
                .andExpect(status().isCreated());
    }

    private ResultActions doRequestSaveComment(final CommentDto dto) throws Exception {
        return getMockMvc()
                .perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper()
                                .writeValueAsString(dto)));
    }

    @Test
    public void whenNullValue_thenReturnBadRequest() throws Exception {

        CommentDto invalidDto = new CommentDto
                (COMMENT_ID, null, null, new Article());

        doRequestSaveComment(invalidDto)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldDeleteCommentById() throws Exception {

        doNothing()
                .when(commentService)
                .delete(COMMENT_ID);

        doRequestDeleteComment()
                .andExpect(status().isOk());
    }

    private ResultActions doRequestDeleteComment() throws Exception {
        return getMockMvc()
                .perform(delete(baseUri + "/{id}", COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldUpdateCommentById() throws Exception {

        Comment updatedComment = new Comment
                (COMMENT_ID, new Article(), "igor@test.com", "This is a simple body",null, LocalDateTime.now());

        when(commentService.update(COMMENT_ID, updatedComment))
                .thenReturn(updatedComment);

        doRequestUpdateComment(commentDto)
                .andExpect(status().isOk());
    }

    @Test
    public void whenNullValueInUpdate_return404() throws Exception {

        CommentDto invalidDto = new CommentDto(COMMENT_ID, null,  "This is text.", new Article());

        doRequestUpdateComment(invalidDto)
                .andExpect(status().isBadRequest());
    }

    private ResultActions doRequestUpdateComment(final CommentDto dto) throws Exception {
        return getMockMvc()
                .perform(put(baseUri + "/{id}", COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getObjectMapper().writeValueAsString(dto)));

    }

    @Test
    public void shouldReturnAllComments() throws Exception {

        doRequestFindAllCommentsByArticleId(ARTICLE_ID)
                .andExpect(status().isOk());

    }
    private ResultActions doRequestFindAllCommentsByArticleId(int articleId) throws Exception {
        return getMockMvc()
                .perform(get("/api/v1/articles/{articleId}/comments", ARTICLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnCommentInBetweenDates() throws Exception {

        final String startDate = "2023-10-23T00:00:00";
        final String endDate = "2023-10-23T23:59:59";

        doRequestFindInBetweenDates(startDate, endDate)
                .andExpect(status().isOk());
    }
    private ResultActions doRequestFindInBetweenDates(final String startDate, final String endDate) throws Exception {

        return getMockMvc()
                .perform(get("/api/v1/comments")
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void ifInvalidDate_throwException() throws Exception {

        final String startDate = "2023-10-23T00:00:00";
        final String invalidEndDate = "2023-10_23T23:59:50";

        doRequestFindInBetweenDates(startDate, invalidEndDate)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testHandleMethodArgumentTypeMismatch() throws Exception {

        getMockMvc().perform(get(baseUri+ "/not_an_int"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testHandleHttpMediaTypeNotSupported() throws Exception {

        getMockMvc().perform(post(baseUri)
                        .accept(MediaType.APPLICATION_XML)
                        .contentType(MediaType.APPLICATION_XML)
                        .content(getObjectMapper().writeValueAsString(this.commentDto)))
                .andExpect(status().isUnsupportedMediaType());

    }

    @Test
    public void testHandleHttpMessageNotReadable() throws Exception {

        final String malformedJson = "{\"key1\": \"value1\", \"key2\": \"value2\", \"key3\":}";

        getMockMvc().perform(post(baseUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testHandleMissingServletRequestArgument() throws Exception {

        getMockMvc().perform(get(baseUri + "/emails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}