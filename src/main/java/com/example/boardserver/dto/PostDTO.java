package com.example.boardserver.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int id;
    private String name;
    private int isAdmin;
    private String contents;
    private Date createTime;
    private Date updateTime;
    private int views;
    private int categoryId;
    private int userId;
}
