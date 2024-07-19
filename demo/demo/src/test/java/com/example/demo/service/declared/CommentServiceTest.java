package com.example.demo.service.declared;

import com.example.demo.config.CustomUserDetailsService;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Game;
import com.example.demo.domain.Member;
import com.example.demo.dto.CommentDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Commit
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static int count = 0;

    @Test
    void save() throws InterruptedException {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("123123123");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> byUsername = memberRepository.findByUsername(name);
        Member member = byUsername.get();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Optional<Game> ticTaeToe = gameRepository.findByGamename("tic_tae_toe");
        Game game = ticTaeToe.get();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    synchronized (this) {
                        count += 1;
                    }
                    Comment comment = Comment.builder()
                            .content("예시텍스트2")
                            .indexNum(count)
                            .depth(1)
                            .member(member)
                            .game(game)
                            .build();

                    commentRepository.save(comment);

                } finally {
                    latch.countDown(); // 작업 완료 후 카운트 다운
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();
    }

}
