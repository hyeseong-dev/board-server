package com.example.boardserver.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostRequest {
    private String name;
    private String contents;
    private int views;
    private int categoryId;
    private int userId;
    private Date updateTime;
}
