package com.example.articles.mapper;

import com.example.articles.constants.ArticleType;
import com.example.articles.dto.ArticleDto;
import com.example.articles.model.Article;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ArticleMapper implements BaseMapper<ArticleDto, Article>{

    private static final ArticleMapper INSTANCE = new ArticleMapper();

    private ArticleMapper(){

    }
    public static ArticleMapper getInstance(){
        return INSTANCE;
    }

    @Override
    public Article fromDto(ArticleDto dto) {
        return new Article.Builder()
                .id(dto.getArticleId())
                .title(dto.getTitle())
                .body(dto.getBody())
                .type(Optional.ofNullable(dto.getArticleType()).orElse(ArticleType.THEORIC))
                .comments(dto.getComments())
                .build();
    }

    @Override
    public ArticleDto fromEntity(Article entity) {
        return new ArticleDto(entity.getId(), entity.getTitle(), entity.getBody(), entity.getArticleType(), entity.getArticleComments());
    }
}
