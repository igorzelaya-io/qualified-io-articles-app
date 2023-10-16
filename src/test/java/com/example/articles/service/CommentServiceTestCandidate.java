package com.example.articles.service;

import com.example.articles.constants.ArticleType;
import com.example.articles.exception.NotFoundException;
import com.example.articles.model.Article;
import com.example.articles.model.Comment;
import com.example.articles.service.impl.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
public class CommentServiceTestCandidate {

    private List<Comment> comments;

    //@MockBean
    private CommentService commentService;

    private Comment comment;

    private Comment secondComment;

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
        commentService = new CommentServiceImpl(comments);
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
    public void whenNotFound_shouldThrowException(){

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

        try{

            Assertions.assertNotNull(updatedComment);
            Assertions.assertEquals(updatedComment.getEmail(), secondComment.getEmail());
            Assertions.assertEquals(updatedComment.getText(), secondComment.getText());

        }
        catch(Exception e){
            Assertions.fail("Comment was not updated at list index.");
        }
    }
}