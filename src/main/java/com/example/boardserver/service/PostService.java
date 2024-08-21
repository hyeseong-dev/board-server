package com.example.boardserver.service;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostRequest;

import java.util.List;

public interface PostService {
    void register(int id, PostRequest postRequest);
    PostDTO updatePost(int postId, int userId, PostRequest postRequest);
    List<PostDTO> getMyPostsWithPaging(int userId, int page, int size);
    int getTotalPages(int userId, int size);
    PostDTO getPost(int postId);

    void deletePost(int userId, int postId);
    void validatePostRequest(PostRequest postRequest);
}
