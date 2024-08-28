package com.example.boardserver.mapper;

import com.example.boardserver.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    Long register(PostDTO postDTO);  // @Param("postDTO") 제거
    List<PostDTO> selectMyPostsWithPaging(@Param("userId") Long userId, @Param("offset") Long offset, @Param("size") Long size);
    void updatePost(@Param("postDTO") PostDTO postDTO);
    void deletePostTagByPostId(@Param("postId") Long postId);
    void deletePost(@Param("postId") Long postId);
    PostDTO getPost(@Param("postId") Long postId);
    Long countMyPosts(@Param("userId") Long userId);
}