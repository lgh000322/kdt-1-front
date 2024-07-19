package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.dto.CommentDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {


}
