package com.example.boardserver.mapper;

import com.example.boardserver.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {
    Long register(CommentDTO commentDTO);
    Long updateComment(@Param("commentDTO") CommentDTO commentDTO, @Param("commentId") Long commentId);
    Long deleteComment(@Param("commentId") Long commentId);
    void deleteCommentsByPostId(@Param("postId") Long postId);



}
