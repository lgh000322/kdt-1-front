package com.example.demo.controller;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberResponseDto;
import com.example.demo.service.declared.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
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


    @GetMapping("/kakao")
    public String redirectFunc(@RequestParam(name = "code") String authorCode, HttpServletRequest request,HttpServletResponse response) {
        String kakaoAccessToken = memberService.getAccessToken(authorCode);

        MemberDto kakaoMember = memberService.getKakaoMember(kakaoAccessToken);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoMember, null, kakaoMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        return prevPage(request, response, savedRequest);
    }

    private String prevPage(HttpServletRequest request, HttpServletResponse response, SavedRequest savedRequest) {
        if (savedRequest == null || savedRequest.getRedirectUrl().contains("login") || savedRequest.getRedirectUrl().contains("member")) {
            return "redirect:/game/init";
        }

        String targetUrl = savedRequest.getRedirectUrl().substring(21, savedRequest.getRedirectUrl().lastIndexOf("?"));
        log.info("targeturl={}", targetUrl);

        requestCache.removeRequest(request, response);
        return "redirect:" + targetUrl;
    }

}
