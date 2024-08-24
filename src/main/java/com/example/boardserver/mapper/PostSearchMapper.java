package com.example.boardserver.mapper;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostSearchMapper {
    Map<String, Object> postSearch(@Param("postSearchRequest") PostSearchRequest postSearchRequest,
                                   @Param("offset") int offset,
                                   @Param("size") int size);
    List<PostDTO> selectPosts(@Param("postSearchRequest") PostSearchRequest postSearchRequest,
                              @Param("offset") int offset,
                              @Param("size") int size);
    int countTotalPosts(@Param("postSearchRequest") PostSearchRequest postSearchRequest);
}