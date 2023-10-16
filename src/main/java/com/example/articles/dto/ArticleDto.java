package com.example.articles.dto;

import com.example.articles.constants.ArticleType;
import com.example.articles.model.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

@JsonSerialize
public class ArticleDto implements Serializable {

    @JsonProperty
    private int id;
    @JsonProperty
    @NotEmpty
    @NotBlank
    private String title;
    @JsonProperty
    @NotEmpty
    @NotBlank
    private String body;
    @JsonProperty
    private ArticleType type;

    @JsonProperty
    private List<Comment> comments;

    public ArticleDto() {}

    public ArticleDto(int id, String title, String body, ArticleType type, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.type = type;
        this.comments = comments;
    }

    public int getArticleId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public ArticleType getArticleType() {
        return type;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
