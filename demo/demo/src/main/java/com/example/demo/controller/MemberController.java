package com.example.demo.controller;

import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberResponseDto;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @PostMapping("/join")
    public String joinProcess(@ModelAttribute MemberJoinDto memberJoinDto) {
        Optional<MemberResponseDto> save = memberService.save(memberJoinDto);

        if (save.isEmpty()) {
            return "redirect:/member/join";
        }

        return "redirect:/member/login";
    }

    @GetMapping("/kakao/login")
    public RedirectView kakaoLogin() {
        return new RedirectView(memberService.getKakaoURI());
    }

    /**
     * Todo
     * 카카오 인가 코드 받는 url 설정하고
     * 인증코드 및 카카오 사용자 정보 받아야함
     * 받고 리턴할 때 세션적용시켜야 함
     */
}
