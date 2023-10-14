package com.example.articles.mapper;

public interface  BaseMapper <D, E>{

     E fromDto(D dto);

     D fromEntity(E entity);

}
