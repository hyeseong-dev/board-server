package com.example.boardserver.dto.request;

import com.example.boardserver.dto.TagDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostRequest {
    private String name;
    private String contents;
    private Long views;
    private Long categoryId;
    private Long userId;
    private Date updateTime;
    private List<TagDTO> tagList;
}
