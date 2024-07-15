package com.example.demo.service;

import com.example.demo.domain.Game;
import com.example.demo.dto.GameDto;
import com.example.demo.repository.GameRepository;
import com.example.demo.service.declared.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Optional<GameDto> findById(Long gameId) {
        return Optional.ofNullable(entityToGameDto(gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("지정한 게임을 찾을 수 없습니다."))));
    }
}
