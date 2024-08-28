package com.example.boardserver.dto;

import com.example.boardserver.dto.request.TagRequest;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagDTO implements Serializable {
    private Long id;
    private String name;
    private String url;
    private Long postId;


    public static TagDTO fromRequestForCreate(TagRequest tagRequest) {
        return TagDTO.builder()
                .name(tagRequest.getName())
                .url(tagRequest.getUrl())
                .build();
    }

    public static TagDTO fromRequestForUpdate(Long tagId, TagRequest tagRequest) {
        return TagDTO.builder()
                .id(tagId)
                .name(tagRequest.getName())
                .url(tagRequest.getUrl())
                .build();
    }
}
