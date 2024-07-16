package com.example.demo.repository;

import com.example.demo.domain.Ranking;

import java.util.List;
import java.util.Optional;

public interface RankingRepositoryCustom {

    Optional<Ranking> findByNickname(String nickname);

    Integer findByGameNameAndNickName(String gamename, String nickname);

    Optional<List<Ranking>> findAllByGameId(Long gameId);

}
