package com.example.demo.controller;

import com.example.demo.dto.RankingDto;
import com.example.demo.service.declared.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/score")
    public Map<String, Object> getScore(@RequestParam(name = "gamename")String gamename,
                                        @RequestParam(name = "nickname") String nickname) {
        Integer score = rankingService.findByGameNameAndNickname(gamename, nickname);
        Map<String, Object> map = new HashMap<>();
        if (score == null) {
            map.put("score", 0);
            return map;
        }
        map.put("score", score);
        return map;
    }

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
}
