package com.example.demo.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CommentDto {
    private String content;

    private Integer IndexNum;

    private Integer depth;

    private String nickname;

    private String gamename;
}
