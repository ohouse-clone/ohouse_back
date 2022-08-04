package com.clone.ohouse.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


public class CommentDto {
    private String title;
    private String content;
}
