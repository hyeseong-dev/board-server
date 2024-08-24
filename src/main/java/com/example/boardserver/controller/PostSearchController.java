package com.example.boardserver.controller;

import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.service.PostSearchService;
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
    public ResponseEntity<CommonResponse<Map<String, Object>>> search(
            @RequestBody PostSearchRequest postSearchRequest,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log.info("Received search request: {}, page: {}, size: {}", postSearchRequest, page, size);
        Map<String, Object> result = postSearchService.searchPosts(postSearchRequest, page, size);

        CommonResponse<Map<String, Object>> commonResponse = new CommonResponse<>(
                HttpStatus.OK,
                "SEARCH_OK",
                "검색이 성공적으로 완료되었습니다.",
                result
        );

        return ResponseEntity.ok(commonResponse);
    }
}
