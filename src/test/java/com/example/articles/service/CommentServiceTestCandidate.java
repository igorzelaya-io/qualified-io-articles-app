package com.example.articles.service;

import com.example.articles.constants.ArticleType;
import com.example.articles.exception.NotFoundException;
import com.example.articles.model.Article;
import com.example.articles.model.Comment;
import com.example.articles.service.impl.ArticleServiceImpl;
import com.example.articles.service.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RunWith(SpringRunner.class)
public class CommentServiceTestCandidate {

    private List<Comment> comments;

    //@MockBean
    private CommentService commentService;

    private ArticleService articleService;

    private Comment comment;

    private Comment secondComment;

    private Article article;

    private static final int ARTICLE_ID = 1;

    private static final int COMMENT_ID = 1;

    private static final int INVALID_ID = 305;

    private static final String COMMENT_EMAIL = "igor@test.com";

    private static final String COMMENT_TEXT = "Nice structured data!!";

    @Before
    public void init(){
        comment = new Comment.Builder()
                .id(COMMENT_ID)
                .text(COMMENT_TEXT)
                .email(COMMENT_EMAIL)
                .id_article(new Article.Builder()
                        .id(1)
                        .title("This is a test Article")
                        .body("This is a test body.")
                        .type(ArticleType.THEORIC)
                        .build())
                .createdAt(LocalDateTime.now())
                .build();


        secondComment = new Comment.Builder()
                .id(2)
                .text(COMMENT_TEXT)
                .email(COMMENT_EMAIL)
                .id_article(new Article.Builder()
                        .id(2)
                        .title("This is a title")
                        .body("This is a test body.")
                        .type(ArticleType.METHODOLOGIC)
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        this.comments = new ArrayList<>(Arrays.asList(comment));

        article = new Article.Builder()
                .id(ARTICLE_ID)
                .title("A complete guide to dependency injection in Spring Boot.")
                .type(ArticleType.CASE_STUDY)
                .body("Make sure you utilize an interface-driven approach in order to achieve loose coupling")
                .comments(this.comments)
                .build();

        articleService = new ArticleServiceImpl(new ArrayList<>(Arrays.asList(article)));

        commentService = new CommentServiceImpl(comments, articleService);
    }

    @Test
    public void shouldReturnCommentById() {

        Comment foundComment = commentService.findById(COMMENT_ID);

        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(foundComment.getId(), COMMENT_ID);
        Assertions.assertEquals(foundComment.getEmail(), COMMENT_EMAIL);
        Assertions.assertEquals(foundComment.getText(), COMMENT_TEXT);

    }

    @Test
    public void whenNotFoundById_shouldThrowException(){

        Map<String, String> params = Map.of("id", String.valueOf(INVALID_ID));

        final String EX_MESSAGE = new StringBuilder("Comment was not found for parameters")
                .append(" ")
                .append(params).toString();

        try {
            Comment foundComment = commentService.findById(INVALID_ID);
            Assertions.fail("Exception should've been thrown.");
        }
        catch(Exception ex) {
            Assertions.assertTrue(ex instanceof NotFoundException);
            Assertions.assertEquals(EX_MESSAGE, ex.getMessage());
        }
    }

    @Test
    public void shouldReturnCommentByEmail() {

        Comment foundComment = commentService.findCommentByEmail(COMMENT_EMAIL);

        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(foundComment.getId(), COMMENT_ID);
        Assertions.assertEquals(foundComment.getEmail(), COMMENT_EMAIL);
        Assertions.assertEquals(foundComment.getText(), COMMENT_TEXT);

    }
    @Test
    public void whenNotFoundByEmail_shouldThrowException() {

        final String INVALID_EMAIL = "invalid@email.net";

        Map<String, String> params = Map.of("email", String.valueOf(INVALID_EMAIL));

        final String EX_MESSAGE = new StringBuilder("Comment was not found for parameters")
                .append(" ")
                .append(params).toString();

        try {
            Comment foundComment = commentService.findCommentByEmail(INVALID_EMAIL);
            Assertions.fail("Exception should've been thrown for invalid email.");
        }
        catch(Exception ex) {
            Assertions.assertTrue(ex instanceof NotFoundException);
            Assertions.assertEquals(EX_MESSAGE, ex.getMessage());
        }
    }

    @Test
    public void shouldReturnAllCommment(){

        List<Comment> foundComments = commentService
                .findAll();

        Assertions.assertEquals(foundComments.size(), comments.size());
        Assertions.assertEquals(foundComments.get(0).getEmail(), comments.get(0).getEmail());
    }

    @Test
    public void shouldSaveComment(){

        Comment savedComment = commentService.save(secondComment);

        Assertions.assertNotNull(savedComment);
        Assertions.assertEquals(savedComment.getId(), 2);
        Assertions.assertEquals(savedComment.getEmail(), secondComment.getEmail());
        Assertions.assertEquals(savedComment.getText(), secondComment.getText());

    }
    @Test
    public void shouldDeleteComment(){

        Map<String, String> params = Map.of("id", String.valueOf(COMMENT_ID));

        final String EX_MESSAGE = new StringBuilder("Comment was not found for parameters")
                .append(" ")
                .append(params).toString();

        commentService.delete(COMMENT_ID);

        try{
            commentService.findById(COMMENT_ID);
            Assertions.fail("Should've deleted the Article with given ID.");
        }
        catch(Exception e){
            Assertions.assertTrue(e instanceof NotFoundException);
            Assertions.assertEquals(EX_MESSAGE, e.getMessage());
        }
    }
    @Test
    public void shouldUpdateComment() {

        Comment updatedComment = commentService.update(COMMENT_ID, secondComment);

        try {
            Assertions.assertNotNull(updatedComment);
            Assertions.assertEquals(updatedComment.getEmail(), secondComment.getEmail());
            Assertions.assertEquals(updatedComment.getText(), secondComment.getText());
        } catch (Exception e) {
            Assertions.fail("Comment was not updated at list index.");
        }
    }
    @Test
    public void shouldRetriveCommentsByArticleId() {
        List<Comment> commentList = commentService.getArticleComments(ARTICLE_ID);
        Assertions.assertTrue(commentList != null);
        Assertions.assertEquals(commentList.size(), this.comments.size());
    }
    @Test
    public void shouldFindCommentsInRange() {

        final LocalDateTime startOfToday = LocalDateTime.parse("2023-10-23T00:00:00");
        final LocalDateTime endOfToday = LocalDateTime.parse("2023-10-23T23:59:59");

        final Comment validComment = new Comment.Builder()
                .id(100)
                .text("This comment was just posted.")
                .email("i@test.com")
                .createdAt(LocalDateTime.parse("2023-10-23T01:00:00"))
                .build();

        comments.add(validComment);
        List<Comment> comments = commentService.findCreatedInBetweenDate(startOfToday, endOfToday);

        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(comments.get(0).getId(), validComment.getId());
        Assertions.assertTrue(comments.stream()
                .allMatch
                        (validatedComment -> isInBetweenDates(validatedComment, startOfToday, endOfToday)));
    }
    @Test
    public void whenInvalidDate_shouldThrowException() {

        try {

            final LocalDateTime startOfToday = LocalDateTime.parse("2023-10-23T00:00:00");
            final LocalDateTime endOfToday = LocalDateTime.parse("2023-10_23T23:59:59");

            List<Comment> comments = this.commentService.findCreatedInBetweenDate(startOfToday, endOfToday);
            Assertions.fail("It should've failed execution for invalid endDate");
        }
        catch(Exception e) {
            Assertions.assertTrue(e instanceof DateTimeParseException);
        }
    }
    private boolean isInBetweenDates(Comment comment, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime createdAt = comment.getCreatedAt();
        return createdAt.isAfter(startDate) && createdAt.isBefore(endDate);
    }

}