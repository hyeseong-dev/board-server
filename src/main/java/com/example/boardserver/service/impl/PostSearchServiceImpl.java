package com.example.boardserver.service.impl;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.mapper.PostSearchMapper;
import com.example.boardserver.mapper.TagMapper;
import com.example.boardserver.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;
    private final TagMapper tagMapper;

    @Async
    @Override
    @Cacheable(value = "getProducts", key = "#root.methodName + '_' + #postSearchRequest.categoryId + '_'  + #postSearchRequest.name + '_' + #page + '_' + #size", unless = "#result.isEmpty()")
    public List<PostDTO> getPosts(PostSearchRequest postSearchRequest, int page, int size) {
        int offset = calculateOffset(page, size);
        try {
            log.info("Searching posts with request: {}, page: {}, size: {}", postSearchRequest, page, size);
            List<PostDTO> postDTOList = postSearchMapper.selectPosts(postSearchRequest, offset, size);
            log.info("Found {} posts", postDTOList.size());
            return postDTOList;
        } catch (Exception e) {
            log.error("Failed to select posts", e);
            throw new RuntimeException("게시글 검색 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public int getTotalCount(PostSearchRequest postSearchRequest) {
        try {
            int count = postSearchMapper.countTotalPosts(postSearchRequest);
            log.info("Total count for request {}: {}", postSearchRequest, count);
            return count;
        } catch (Exception e) {
            log.error("Failed to get total count", e);
            throw new RuntimeException("전체 게시글 수 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Cacheable(value = "searchPosts", key = "#root.methodName + '_' + #postSearchRequest.categoryId + '_' + #postSearchRequest.name + '_' + #page + '_' + #size", unless = "#result.isEmpty()")
    public Map<String, Object> searchPosts(PostSearchRequest postSearchRequest, int page, int size) {
        try {
            List<PostDTO> posts = getPosts(postSearchRequest, page, size);
            int totalCount = getTotalCount(postSearchRequest);
            setTagsForPosts(posts);
            return createSearchResult(posts, page, size, totalCount);
        } catch (Exception e) {
            log.error("Failed to search posts", e);
            throw new RuntimeException("게시글 검색 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Cacheable(value = "searchPostsByTag", key = "#root.methodName + '_' + #tagName + '_' + #page + '_' + #size", unless = "#result.isEmpty()")
    public Map<String, Object> searchPostsByTag(String tagName, int page, int size) {
        int offset = calculateOffset(page, size);
        try {
            List<PostDTO> posts = postSearchMapper.selectPostsByTag(tagName, offset, size);
            int totalCount = postSearchMapper.countTotalPostsByTag(tagName);
            setTagsForPosts(posts);
            return createSearchResult(posts, page, size, totalCount);
        } catch (Exception e) {
            log.error("Failed to search posts by tag", e);
            throw new RuntimeException("태그로 게시글 검색 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public int getTotalPages(PostSearchRequest postSearchRequest, int size) {
        int totalPosts = getTotalCount(postSearchRequest);
        int totalPages = calculateTotalPages(totalPosts, size);
        log.info("Total pages for request {} with size {}: {}", postSearchRequest, size, totalPages);
        return totalPages;
    }

    private int calculateOffset(int page, int size) {
        return (page - 1) * size;
    }

    private int calculateTotalPages(int totalCount, int size) {
        return (int) Math.ceil((double) totalCount / size);
    }

    private void setTagsForPosts(List<PostDTO> posts) {
        for (PostDTO post : posts) {
            List<TagDTO> tagList = tagMapper.findTagsByPostId(post.getId());
            post.setTagList(tagList);
        }
    }

    private Map<String, Object> createSearchResult(List<PostDTO> posts, int page, int size, int totalCount) {
        int totalPages = calculateTotalPages(totalCount, size);
        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);
        log.info("Search result: posts size = {}, totalCount = {}, totalPages = {}", posts.size(), totalCount, totalPages);
        return result;
    }
}