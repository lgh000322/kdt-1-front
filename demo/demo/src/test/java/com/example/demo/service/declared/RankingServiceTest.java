package com.example.demo.service.declared;

import com.example.demo.domain.Ranking;
import com.example.demo.dto.RankingDto;
import com.example.demo.repository.RankingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Slf4j
class RankingServiceTest {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RankingRepository rankingRepository;

    /**
     * 가상의 시나리오: 만약 동일한 회원이 같은 튜플에 대한 업데이트 호출이 가능하다고 가정.(실제론 이런 일이 발생하지 않음)
     * 같은 회원 100명이 업데이트를 동시에 수행하면 score+=100이 되야함.
     * 한번에 접속할 수 있는 회원을 32명으로 하기위해 쓰레드풀을 32로 설정함
     * rankingdto의 setscore는 critical section에 포함시켜야해서 synchronized 예약어를 사용함
     * @throws InterruptedException
     */
    @Transactional
    @Test
    void 점수_업데이트_동시성_테스트() throws InterruptedException {
        String memberNickname = "newmember";
        RankingDto rankingDto = new RankingDto();
        rankingDto.setGamename("tic_tae_toe");
        rankingDto.setNickname(memberNickname);
        rankingDto.setScore(2);

        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    rankingService.update(rankingDto);
                    synchronized (this) {
                        rankingDto.setScore(rankingDto.getScore() + 1);
                    }
                } finally {
                    latch.countDown(); // 작업 완료 후 카운트 다운
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        Ranking ranking = rankingRepository.findById(4L).orElseThrow();

        assertThat(ranking.getScore()).isEqualTo(101);
    }
}
