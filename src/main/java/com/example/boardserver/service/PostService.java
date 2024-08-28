package com.example.boardserver.service;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.CommentRequest;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.dto.request.TagRequest;

import java.util.Map;

public interface PostService {
    PostDTO register(Long id, PostRequest postRequest);
    PostDTO updatePost(Long postId, Long userId, PostRequest postRequest);
    Map<String, Object> getMyPosts(Long userId, Long page, Long size);
    Long getTotalPages(Long userId, Long size);
    PostDTO getPost(Long postId);

    void deletePost(Long userId, Long postId);
    void validatePostRequest(PostRequest postRequest);

    // -- comment
    CommentDTO registerComment(Long userId, CommentRequest commentRequest);
    void updateComment(Long userId,  Long commentId,CommentRequest commentRequest);
    void deleteComment(Long userId, Long commentId);

    // -- tag
    void registerTag(Long userId, TagRequest tagRequest);
    void updateTag(Long userId, Long tagId, TagRequest tagRequest);
    void deleteTag(Long userId, Long tagId);
}
