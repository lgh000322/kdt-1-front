package com.example.demo.repository;

import com.example.demo.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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
    public Optional<Ranking> findByGameNameAndNickName(String gamename, String nickname) {
        //회원의 닉네임과 게임의 이름이 같은것
        Ranking findRanking = queryFactory
                .select(ranking)
                .from(ranking)
                .innerJoin(ranking.game, game)
                .innerJoin(ranking.member, member)
                .fetchJoin()
                .where(
                        ranking.member.nickname.eq(nickname),
                        ranking.game.gamename.eq(gamename)
                )
               /* .setLockMode(LockModeType.PESSIMISTIC_WRITE)*/
                .fetchOne();

        return Optional.ofNullable(findRanking);
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
