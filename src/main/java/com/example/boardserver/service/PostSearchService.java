package com.example.boardserver.service;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchByTagRequest;
import com.example.boardserver.dto.request.PostSearchRequest;

import java.util.List;
import java.util.Map;

public interface PostSearchService {
    Map<String, Object> searchPosts(PostSearchRequest postSearchRequest, int page, int size);
    Map<String, Object> searchPostsByTag(String tagName, int page, int size);
    List<PostDTO> getPosts(PostSearchRequest postSearchRequest, int page, int size);
    int getTotalPages(PostSearchRequest postSearchRequest, int size);
    int getTotalCount(PostSearchRequest postSearchRequest);
}
