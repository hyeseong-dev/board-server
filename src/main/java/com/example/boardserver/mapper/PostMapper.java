package com.example.boardserver.mapper;

import com.example.boardserver.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    int register(@Param("postDTO") PostDTO postDTO);
    List<PostDTO> selectMyPostsWithPaging(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);
    void updatePost(@Param("postDTO") PostDTO postDTO);
    void deletePost(@Param("postId") int postId);
    PostDTO getPost(@Param("postId") int postId);
    int countMyPosts(@Param("userId") int userId);
}