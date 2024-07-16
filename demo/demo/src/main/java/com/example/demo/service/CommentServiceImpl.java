package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Game;
import com.example.demo.domain.Member;
import com.example.demo.dto.CommentDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.declared.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public boolean save(CommentDto commentDto) {
        Member member = getMember();
        Game game = getGame(commentDto);

        Integer lastIndex = commentRepository.getLastIndex(commentDto.getGamename());
        Integer depth = 1;

        commentDto.setIndexNum(lastIndex);
        commentDto.setDepth(depth);

        Comment comment = getComment(commentDto, member, game);
        Comment save = commentRepository.save(comment);

        return save != null;
    }

    @Override
    public Optional<List<CommentDto>> findByGameId(Long gameId) {
        Optional<List<Comment>> foundedGame = commentRepository.findByGameId(gameId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        foundedGame.ifPresent((comments)->{
            for (Comment comment : comments) {
                commentDtoList.add(entityToCommentDto(comment));
            }
        });

        return Optional.ofNullable(commentDtoList);
    }

    private Comment getComment(CommentDto commentDto, Member member, Game game) {
        Comment comment = commentDtoToEntity(commentDto);
        comment.setGame(game);
        comment.setMember(member);
        return comment;
    }

    private Game getGame(CommentDto commentDto) {
        return gameRepository.findByGamename(commentDto.getGamename())
                .orElseThrow(() -> new RuntimeException("해당 게임을 찾을 수 없습니다."));
    }

    private Member getMember() {
        return memberRepository.findByUsername(SecurityContextHolder
                        .getContextHolderStrategy().getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));
    }
}
