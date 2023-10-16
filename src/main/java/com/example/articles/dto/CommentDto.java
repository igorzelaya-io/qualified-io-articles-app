package com.example.articles.dto;

import com.example.articles.model.Article;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@JsonSerialize
public class CommentDto {
    @JsonProperty
    private int id;
    @JsonProperty
    @NotEmpty
    @NotBlank
    @Email(message = "Email format is incorrect.")
    private String email;
    @JsonProperty
    @NotEmpty
    @NotBlank
    private String text;

    @JsonProperty
    private Article id_article;

    public CommentDto() {
    }

    public CommentDto(int id, String email, String text, Article id_article) {
        this.id = id;
        this.email = email;
        this.text = text;
    }

    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }

    public Article getId_article() {
        return id_article;
    }

}
