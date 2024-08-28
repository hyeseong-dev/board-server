package com.example.boardserver.mapper;

import com.example.boardserver.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    Long register(TagDTO tagDTO);
    Long updateTag(@Param("tagId") Long tagId, @Param("tagDTO") TagDTO tagDTO);
    Long deleteTag(@Param("tagId") Long tagId);
    Long createPostTag(@Param("tagId") Long tagId, @Param("postId") Long postId);
    // 태그 이름으로 태그 검색
    TagDTO findTagByName(@Param("name") String name);
    // 새로 추가: 게시글 ID로 태그 리스트 조회
    List<TagDTO> findTagsByPostId(@Param("postId") Long postId);
    TagDTO findById(@Param("tagId") Long tagId);
}
