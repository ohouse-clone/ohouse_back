package com.clone.ohouse.community.dto;

import com.clone.ohouse.community.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostDetailResponseDTO extends PostResponseDTO {
    private LocalDateTime modDate;

    public PostDetailResponseDTO(Post post){
//        super(post);
        this.modDate = post.getModifiedDate();
    }
}
