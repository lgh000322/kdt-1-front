package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.GameDto;
import com.example.demo.service.declared.CommentService;
import com.example.demo.service.declared.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
@Slf4j
public class GameController {

    private final GameService gameService;
    private final CommentService commentService;
    @GetMapping("/init")
    public String init(Model model) {
        String name = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
        log.info("name={}", name);
        if (name.equals("anonymousUser")) {
            return "init";
        }
        model.addAttribute("memberName", name);
        return "init";
    }

    @GetMapping("/{gameId}")
    public String getGame(@PathVariable(name = "gameId") Long gameId, Model model) {
        StringBuffer sb = new StringBuffer();
        String name = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName();
        Optional<List<CommentDto>> foundedGame = commentService.findByGameId(gameId);

        gameService.findById(gameId).ifPresentOrElse((game) -> {
            model.addAttribute("gameName", game.getGamename());
            sb.append("games_");
            sb.append(game.getGamename());
        }, () -> {
            new RuntimeException("해당 게임을 찾을 수 없습니다.");
        });

        List<CommentDto> commentDtoList = foundedGame.orElseThrow();

        //회원의 이름
        model.addAttribute("memberName", name);

        //댓글 목록
        model.addAttribute("commentList", commentDtoList);

        return sb.toString();
    }

}
