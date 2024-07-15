package com.example.demo.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberJoinDto {
    private String id;
    private String pw;
    private String name;

}
