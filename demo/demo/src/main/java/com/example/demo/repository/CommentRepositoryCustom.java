package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {

    Integer getLastIndex(String gamename);

    Optional<List<Comment>> findByGameId(Long gameId);

}
