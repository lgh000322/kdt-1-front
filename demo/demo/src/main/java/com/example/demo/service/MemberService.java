package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberResponseDto;

import java.util.Optional;
import java.util.stream.Collectors;

public interface MemberService {
    Optional<MemberResponseDto> save(MemberJoinDto memberJoinDto);

    String getKakaoURI();

    String getAccessToken(String authorCode);

    MemberDto getKakaoMember(String accessToken);
    boolean isExistsByUsername(String username);

    boolean isExistsByNickname(String nickname);

    default Member memberJoinDtoToEntity(MemberJoinDto memberJoinDto) {

        return Member.builder()
                .username(memberJoinDto.getId())
                .password(memberJoinDto.getPw())
                .nickname(memberJoinDto.getName())
                .build();
    }

    default MemberResponseDto entityToMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }

    default MemberDto entityToMemberDto(Member member) {
        return new MemberDto(
                member.getUsername(),
                member.getPassword(),
                member.getNickname(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
    }
}
