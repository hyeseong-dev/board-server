package com.example.boardserver.dto;

import com.example.boardserver.dto.request.PostRequest;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {
    private Long id;
    private String name;
    private Long isAdmin;
    private String contents;
    private Date createTime;
    private Date updateTime;
    private Long views;
    private Long categoryId;
    private Long userId;

    @Builder.Default
    private List<TagDTO> tagList = new ArrayList<>();

    public void setTagList(List<TagDTO> tagList) {
        if (tagList != null) {
            this.tagList = tagList;
        }
    }

    public static PostDTO fromRequest(Long userId, PostRequest postRequest) {
        return PostDTO.builder()
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(userId)
                .createTime(new Date())
                .updateTime(new Date())
                .tagList(postRequest.getTagList())
                .build();
    }
}
