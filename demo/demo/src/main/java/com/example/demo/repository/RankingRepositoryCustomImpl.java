package com.example.demo.repository;

import com.example.demo.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.QGame.*;
import static com.example.demo.domain.QMember.*;
import static com.example.demo.domain.QRanking.*;

public class RankingRepositoryCustomImpl extends QuerydslRepositorySupport implements RankingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RankingRepositoryCustomImpl(EntityManager entityManager) {
        super(Ranking.class);
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Ranking> findByNickname(String nickname) {
        //회원의 닉네임
        Ranking foundedRanking = queryFactory
                .select(ranking)
                .from(ranking)
                .innerJoin(ranking.member, member).fetchJoin()
                .where(member.nickname.eq(nickname))
                .fetchOne();

        return Optional.ofNullable(foundedRanking);
    }

    @Override
    public Integer findByGameNameAndNickName(String gamename, String nickname) {
        //회원의 닉네임과 게임의 이름이 같은것의 점수
        Integer score = queryFactory
                .select(ranking.score)
                .from(ranking)
                .innerJoin(ranking.game, game)
                .innerJoin(ranking.member, member)
                .where(
                        ranking.member.nickname.eq(nickname),
                        ranking.game.gamename.eq(gamename)
                )
                .fetchOne();

        return score;
    }

    @Override
    public Optional<List<Ranking>> findAllByGameId(Long gameId) {
        return Optional.ofNullable(queryFactory
                .select(ranking)
                .from(ranking)
                .innerJoin(ranking.game, game)
                .where(ranking.game.id.eq(gameId))
                .orderBy(ranking.score.desc())
                .limit(10)
                .offset(0)
                .fetch());
    }
}
