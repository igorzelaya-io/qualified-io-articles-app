package com.example.articles.mapper;

import com.example.articles.dto.CommentDto;
import com.example.articles.model.Comment;
import org.springframework.stereotype.Component;


@Component
public class CommentMapper implements BaseMapper<CommentDto, Comment>{

    private static final CommentMapper INSTANCE = new CommentMapper();
    private CommentMapper(){
    }
    public static CommentMapper getInstance(){
        return INSTANCE;
    }
    @Override
    public Comment fromDto(CommentDto dto) {
        return new Comment.Builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .id_article(dto.getId_article())
                .text(dto.getText())
                .build();
    }
    @Override
    public CommentDto fromEntity(Comment entity) {
        return new CommentDto(entity.getId(), entity.getEmail(), entity.getText(), entity.getId_article());
    }
}
