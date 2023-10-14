package com.example.articles.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment implements Comparable<Comment>{
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(referencedColumnName="id", nullable = false)
    private Article id_article;
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Comment(int id, Article id_article, String email, String text, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.id_article = id_article;
        this.email = email;
        this.text = text;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    protected Comment() {

    }

    public int getId() {
        return id;
    }

    protected void setId(int id){
        this.id = id;
    }

    public Article getId_article() {
        return id_article;
    }

    protected void setId_article(Article article){
        this.id_article = article;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email){
        this.email = email;
    }

    public String getText() {
        return text;
    }

    protected void setText(String text){
        this.text = text;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int compareTo(Comment o) {
        return Integer.compare(this.id, o.getId());
    }

    public static class Builder {
        private int id;
        private Article id_article;
        private String text;
        private String email;
        private LocalDateTime updatedAt;

        private LocalDateTime createdAt;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt){
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt){
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder id_article(Article article){
            this.id_article = article;
            return this;
        }
        public Comment build() {
            Comment comment = new Comment();
            comment.setId(this.id);
            comment.setId_article(this.id_article);
            comment.setEmail(this.email);
            comment.setText(this.text);
            comment.setUpdatedAt(this.updatedAt);
            comment.setCreatedAt(this.createdAt);
            return comment;
        }
    }
    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .text(this.text)
                .email(this.email)
                .id_article(this.id_article)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt);
    }
}
