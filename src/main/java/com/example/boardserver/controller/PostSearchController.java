package com.example.boardserver.controller;

import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.dto.request.PostSearchByTagRequest;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.service.PostSearchService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class PostSearchController {
    private final PostSearchService postSearchService;

    @PostMapping
    public ResponseEntity<CommonResponse<Map<String, Object>>> searchPosts(
            @RequestBody PostSearchRequest postSearchRequest,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        log.info("Received searchPosts request: {}, page: {}, size: {}", postSearchRequest, page, size);
        Map<String, Object> result = postSearchService.searchPosts(postSearchRequest, page, size);

        CommonResponse<Map<String, Object>> commonResponse = new CommonResponse<>(
                HttpStatus.OK,
                "SEARCH_POSTS_OK",
                "검색이 성공적으로 완료되었습니다.",
                result
        );

        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Map<String, Object>>> searchPostsByTag(
            @NotBlank String tagName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        log.info("Received searchPostsByTag tagName: {}, page: {}, size: {}", tagName, page, size);
        Map<String, Object> result = postSearchService.searchPostsByTag(tagName, page, size);

        CommonResponse<Map<String, Object>> commonResponse = new CommonResponse<>(
                HttpStatus.OK,
                "SEARCH_POSTS_BY_TAG_OK",
                "태그 검색이 성공적으로 완료되었습니다.",
                result
        );

        return ResponseEntity.ok(commonResponse);
    }
}
