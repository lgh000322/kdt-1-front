package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.MemberRole;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberJoinDto;
import com.example.demo.dto.MemberResponseDto;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.KakaoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final KakaoUtils kakaoUtils;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Optional<MemberResponseDto> save(MemberJoinDto memberJoinDto) {
        memberJoinDto.setPw(passwordEncoder.encode(memberJoinDto.getPw()));
        Member member = memberJoinDtoToEntity(memberJoinDto);
        member.addRole(MemberRole.USER);

        Member save = memberRepository.save(member);
        return Optional.ofNullable(entityToMemberResponseDto(save));
    }

    @Override
    public String getKakaoURI() {
        return kakaoUtils.getKakaoURI();
    }

    @Override
    public String getAccessToken(String authorCode) {
        return kakaoUtils.getKakaoAccessToken(authorCode);
    }

    @Transactional
    @Override
    public MemberDto getKakaoMember(String accessToken) {
        String nickname = kakaoUtils.getKakaoUserInfo(accessToken);
        log.info("======================================================");
        log.info("카카오 유저의 nickname={}", nickname);

        Optional<Member> result = memberRepository.findByUsername(nickname);

        //이미 가입했다면
        if (result.isPresent()) {
            return entityToMemberDto(result.orElseThrow());
        }

        //username에 nickname을 주게끔 구현
        Member socialMember = makeSocialMember(nickname);
        memberRepository.save(socialMember);
        return entityToMemberDto(socialMember);
    }

    private Member makeSocialMember(String username) {

        String tempPassword = passwordEncoder.encode(UUID.randomUUID().toString());

        log.info("tempPassword={}", tempPassword);

        Member member = Member.builder()
                .username(username)
                .password(tempPassword)
                .nickname(username)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    public boolean isExistsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

}
