package com.example.boardserver.service.impl;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.mapper.PostSearchMapper;
import com.example.boardserver.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;

    @Override
    @Cacheable(value = "getProducts",
            key = "'getProducts_' + #postSearchRequest.categoryId + '_' + #postSearchRequest.sortStatus + '_page' + #page + '_size' + #size",
            unless = "#result.isEmpty()")
    public List<PostDTO> getPosts(PostSearchRequest postSearchRequest, int page, int size) {
        int offset = (page - 1) * size;
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

    @Cacheable(value = "searchPosts",
            key = "'searchPosts_' + #postSearchRequest.categoryId + '_' + #postSearchRequest.sortStatus + '_page' + #page + '_size' + #size",
            unless = "#result.isEmpty()")
    @Override
    public Map<String, Object> searchPosts(PostSearchRequest postSearchRequest, int page, int size) {
        int offset = (page - 1) * size;
        List<PostDTO> posts = postSearchMapper.selectPosts(postSearchRequest, offset, size);
        int totalCount = postSearchMapper.countTotalPosts(postSearchRequest);

        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);

        log.info("Search result: posts size = {}, totalCount = {}, totalPages = {}",
                posts.size(), totalCount, totalPages);
        return result;
    }

    @Override
    public int getTotalPages(PostSearchRequest postSearchRequest, int size) {
        int totalPosts = this.getTotalCount(postSearchRequest);
        int totalPages = (int) Math.ceil((double) totalPosts / size);
        log.info("Total pages for request {} with size {}: {}", postSearchRequest, size, totalPages);
        return totalPages;
    }
}