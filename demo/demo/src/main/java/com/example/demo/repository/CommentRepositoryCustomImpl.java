package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.domain.QComment;
import com.example.demo.domain.QGame;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.QComment.*;
import static com.example.demo.domain.QGame.*;

public class CommentRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public CommentRepositoryCustomImpl(EntityManager entityManager) {
        super(Comment.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Integer getLastIndex(String gamename) {
        Integer lastIndex = queryFactory
                .select(comment.indexNum.max())
                .from(comment)
                .leftJoin(comment.game, game)
                .where(comment.game.gamename.eq(gamename))
                .fetchOne();

        if (lastIndex == null) {
            return 1;
        }

        return lastIndex+1;
    }

    @Override
    public Optional<List<Comment>> findByGameId(Long gameId) {
        List<Comment> result = queryFactory
                .select(comment)
                .from(comment)
                .leftJoin(comment.game, game).fetchJoin()
                .where(comment.game.id.eq(gameId))
                .fetch();

        return Optional.ofNullable(result);
    }
}
