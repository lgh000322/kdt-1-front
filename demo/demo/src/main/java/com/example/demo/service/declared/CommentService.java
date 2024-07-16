package com.example.demo.service.declared;

import com.example.demo.domain.Comment;
import com.example.demo.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    boolean save(CommentDto commentDto);

    Optional<List<CommentDto>> findByGameId(Long gameId);

    default CommentDto entityToCommentDto(Comment comment) {
        return CommentDto.builder()
                .content(comment.getContent())
                .IndexNum(comment.getIndexNum())
                .depth(comment.getDepth())
                .nickname(comment.getMember().getNickname())
                .gamename(comment.getGame().getGamename())
                .build();
    }
    default Comment commentDtoToEntity(CommentDto commentDto) {
        return Comment.builder()
                .content(commentDto.getContent())
                .indexNum(commentDto.getIndexNum())
                .depth(commentDto.getDepth())
                .build();
    }
}
