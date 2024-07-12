package com.example.demo.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomLoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String id = request.getParameter("id");
        log.info("사용자가 입력한 id= {}", id);

        // 실패 이유를 모델에 추가하여 로그인 페이지로 리다이렉트
        request.getSession().setAttribute("loginErrorMessage", "Invalid username or password.");
        request.getSession().setAttribute("id", id); // 이전에 입력한 아이디 유지

        super.setDefaultFailureUrl("/login"); // 로그인 페이지 URL 설정
        super.onAuthenticationFailure(request, response, exception);
    }
}
