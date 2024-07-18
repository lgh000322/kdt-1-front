package com.example.demo.controller;

import com.example.demo.dto.RankingDto;
import com.example.demo.service.declared.GameService;
import com.example.demo.service.declared.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;
    private final GameService gameService;

    @ResponseBody
    @GetMapping("/score")
    public Map<String, Object> getScore(@RequestParam(name = "gamename") String gamename,
                                        @RequestParam(name = "nickname") String nickname) {
        Integer score = rankingService.findScoreByGameNameAndNickname(gamename, nickname);
        Map<String, Object> map = new HashMap<>();
        if (score == null) {
            map.put("score", 0);
            return map;
        }
        map.put("score", score);
        return map;
    }

    @ResponseBody
    @PostMapping("/score")
    public Map<String, Object> updateScore(@RequestBody RankingDto rankingDto) {
        Integer score = rankingDto.getScore();
        Map<String, Object> map = new HashMap<>();
        log.info("컨트롤러에 들어온 점수={}", rankingDto.getScore());

        map.put("success", true);
        map.put("score", score);

        if (rankingDto.getScore() == 1) {
            rankingService.save(rankingDto);
            return map;
        }

        rankingService.update(rankingDto);
        return map;
    }

    @GetMapping("/list/{gameId}")
    public String rankingPage(@PathVariable(name = "gameId") Long gameId, Model model) {
        String gamename = gameService.findById(gameId).orElseThrow().getGamename();

        StringBuffer sb = new StringBuffer();
        sb.append("ranking_");
        sb.append(gamename);

        Optional<List<RankingDto>> foundedRankings = rankingService.findByGameId(gameId);
        List<RankingDto> rankingDtos = foundedRankings.orElseThrow();

        model.addAttribute("gameTitleRanking", gamename + "의 랭킹");
        model.addAttribute("rankingList", rankingDtos);

        return sb.toString();
    }
}
