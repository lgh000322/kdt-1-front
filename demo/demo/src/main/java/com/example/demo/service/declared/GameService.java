package com.example.demo.service.declared;

import com.example.demo.domain.Game;
import com.example.demo.dto.GameDto;

import java.util.Optional;

public interface GameService {

    //id로 특정 게임 찾기
    Optional<GameDto> findById(Long gameId);

    default GameDto entityToGameDto(Game game) {

        return GameDto.builder()
                .gamename(game.getGamename())
                .build();
    }
}
