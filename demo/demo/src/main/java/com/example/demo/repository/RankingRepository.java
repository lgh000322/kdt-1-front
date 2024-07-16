package com.example.demo.repository;

import com.example.demo.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long>,RankingRepositoryCustom {

}
