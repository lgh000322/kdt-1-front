package com.example.demo.config;

import com.example.demo.config.handler.CustomLoginFailHandler;
import com.example.demo.config.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLoginFailHandler customLoginFailHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:8080"));
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/member/join", "/member/idValidation", "/member/nameValidation","/member/kakao/login","/member/kakao","/game/init", "/css/**","/js/**","/img/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(login ->
                        login
                                .loginPage("/member/login")
                                .loginProcessingUrl("/member/login")
                                .usernameParameter("id")
                                .passwordParameter("pw")
                                .successHandler(customLoginSuccessHandler)
                                .failureHandler(customLoginFailHandler)
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL 지정
                        .logoutSuccessUrl("/member/login")  // 로그아웃 성공 시 이동할 URL 지정
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")// 쿠키 삭제
                        .permitAll());

        return httpSecurity.build();
    }
}
