package com.example.demo.controller;

import com.example.demo.config.CustomUserDetailsService;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberResponseDto;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginProc() {
        return "init";
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
    @GetMapping("/kakao")
    public String redirectFunc(@RequestParam(name = "code") String authorCode, HttpServletRequest request) {
        // Step 1: Obtain Kakao access token
        String kakaoAccessToken = memberService.getAccessToken(authorCode);

        // Step 2: Obtain Kakao member information
        MemberDto kakaoMember = memberService.getKakaoMember(kakaoAccessToken);

        // Step 3: Load UserDetails
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(kakaoMember.getUsername());

        // Step 4: Create UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Step 5: Authenticate the user
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Step 6: Store the SecurityContext in session
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return "redirect:/game/init";
    }

}
