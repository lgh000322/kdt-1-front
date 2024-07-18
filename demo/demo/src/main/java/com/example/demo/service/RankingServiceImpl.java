package com.example.demo.service;

import com.example.demo.domain.Game;
import com.example.demo.domain.Member;
import com.example.demo.domain.Ranking;
import com.example.demo.dto.MemberResponseDto;
import com.example.demo.dto.RankingDto;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RankingRepository;
import com.example.demo.service.declared.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    @Transactional
    @Override
    public Optional<RankingDto> save(RankingDto rankingDto) {
        Game game = getGame(rankingDto);
        Member member = getMember();
        Ranking ranking = getRanking(rankingDto, game, member);

        return Optional.ofNullable(entityToRankingDto(rankingRepository.save(ranking)));
    }

    @Transactional
    @Override
    public Optional<RankingDto> update(RankingDto rankingDto) {
        Optional<Ranking> foundedRanking = rankingRepository.findByGameNameAndNickName(rankingDto.getGamename(), rankingDto.getNickname());
        Ranking ranking = foundedRanking.orElseThrow(() ->
                new RuntimeException("회원이 가진 점수가 존재하지 않습니다."));
        ranking.updateScore(rankingDto.getScore());
        return Optional.ofNullable(entityToRankingDto(ranking));
    }

    @Override
    public Optional<List<RankingDto>> findByGameId(Long gameId) {
        List<Ranking> rankings = rankingRepository.findAllByGameId(gameId).orElseThrow();

        List<RankingDto> rankingDtos = new ArrayList<>();

        for (Ranking ranking : rankings) {
            rankingDtos.add(entityToRankingDto(ranking));
        }

        return Optional.ofNullable(rankingDtos);
    }

    @Override
    public Integer findScoreByGameNameAndNickname(String gamename, String nickname) {
        Optional<Ranking> foundedRanking = rankingRepository.findByGameNameAndNickName(gamename, nickname);
        return foundedRanking.map(Ranking::getScore).orElse(null);
    }

    private Ranking getRanking(RankingDto rankingDto, Game game, Member member) {
        Ranking ranking = RankingDtoToEntity(rankingDto);
        ranking.setGame(game);
        ranking.setMember(member);
        return ranking;
    }

    private Member getMember() {
        String name = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
        Optional<Member> foundedMember = memberRepository.findByUsername(name);
        Member member = foundedMember.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return member;
    }

    private Game getGame(RankingDto rankingDto) {
        Optional<Game> foundedGame = gameRepository.findByGamename(rankingDto.getGamename());
        Game game = foundedGame.orElseThrow(() -> new RuntimeException("게임을 찾을 수 없습니다."));
        return game;
    }

}
