package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    @EntityGraph(attributePaths = "memberRoleList")
    @Query("select m from Member m where m.username = :username")
    Optional<Member> getWithRoles(@Param("username") String username);

    Optional<Member> findByUsername(String username);
}
