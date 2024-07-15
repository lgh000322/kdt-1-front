package com.example.demo.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberDto extends User {
    private String username;

    private String password;

    private String nickname;

    private List<String> roleNames = new ArrayList<>();

    public MemberDto(String username, String password, String nickname, List<String> roleNames) {
        super(
                username,
                password,
                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList())
        );

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roleNames = roleNames;
    }

    public Map<String,Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", username);
        dataMap.put("nickname", nickname);
        dataMap.put("roleNames", roleNames);
        return dataMap;
    }
}
