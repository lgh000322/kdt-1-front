package com.example.demo.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RankingDto {

    private Integer score;

    private String gamename;

    private String nickname;
}
