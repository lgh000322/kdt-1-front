package com.example.demo.service.declared;

import com.example.demo.domain.Ranking;
import com.example.demo.dto.RankingDto;

import java.util.Optional;

public interface RankingService {

    Optional<RankingDto> save(RankingDto rankingDto);

    Optional<RankingDto> update(RankingDto rankingDto);

    Integer findByGameNameAndNickname(String gamename, String nickname);

    default Ranking RankingDtoToEntity(RankingDto rankingDto) {
        return Ranking.builder()
                .score(rankingDto.getScore())
                .build();
    }

    default RankingDto entityToRankingDto(Ranking ranking) {
        return RankingDto.builder()
                .score(ranking.getScore())
                .gamename(ranking.getGame().getGamename())
                .nickname(ranking.getMember().getNickname())
                .build();
    }
}
