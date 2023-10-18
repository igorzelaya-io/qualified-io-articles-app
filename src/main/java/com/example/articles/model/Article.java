package com.example.articles.model;

import com.example.articles.constants.ArticleType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Article implements Comparable<Article> {

    @Id
    @Column(insertable = false, updatable = false)
    private int id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "body", nullable = false)
    private String body;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleType type;
    @OneToMany(mappedBy = "id_article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Article(int id, String title, String body, ArticleType type, List<Comment> articleComments) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.type = type;
        this.comments = articleComments;
    }

    public Article() {

    }

    public int getId() {
        return this.id;
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

    public List<Comment> getArticleComments() {
        return comments;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setBody(String body) {
        this.body = body;
    }

    protected void setType(ArticleType type) {
        this.type = type;
    }

    protected void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setId_article(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setId_article(null);
    }

    @Override
    public int compareTo(Article o) {
        return Integer.compare(this.id, o.getId());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        Article article = (Article) obj;
        return this.id == article.id && this.title.equals(article.getTitle());
    }

    public static class Builder {
        private int id;
        private String title;
        private String body;
        private ArticleType articleType;
        private List<Comment> comments = new ArrayList<>();

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder type(ArticleType articleType) {
            this.articleType = articleType;
            return this;
        }

        public Builder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Article build() {
            Article article = new Article();
            article.setId(id);
            article.setTitle(title);
            article.setBody(body);
            article.setType(articleType);
            for (Comment comment : comments) {
                article.addComment(comment);
            }
            return article;
        }
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .body(this.body)
                .title(this.title)
                .type(this.type)
                .comments(this.comments);
    }
}