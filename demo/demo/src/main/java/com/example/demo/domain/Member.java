package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String nickname;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    public void clearRole() {
        memberRoleList.clear();
    }
}
