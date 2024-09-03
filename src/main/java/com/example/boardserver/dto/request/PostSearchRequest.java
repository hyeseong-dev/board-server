package com.example.boardserver.dto.request;

import com.example.boardserver.dto.CategoryDTO;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchRequest {
    private int id;
    private String name;
    private String contents;
    private int views;
    private int categoryId;
    private int userId;

    @Builder.Default
    private CategoryDTO.SortStatus sortStatus = CategoryDTO.SortStatus.NEWEST;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;
}
