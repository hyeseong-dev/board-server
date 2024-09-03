package com.example.boardserver.controller;

import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.request.PostSearchByTagRequest;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.service.PostSearchService;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
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
    private final UserService userService;

    // 공통 응답 생성 메서드
    private <T> ResponseEntity<CommonResponse<T>> createResponse(HttpStatus status, String code, String message, T data) {
        return ResponseEntity.status(status)
                .body(new CommonResponse<>(status, code, message, data));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Map<String, Object>>> searchPosts(
            @RequestBody PostSearchRequest postSearchRequest,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            HttpSession session
    ) {

        log.info("Received searchPosts request: {}, page: {}, size: {}", postSearchRequest, page, size);
        Map<String, Object> result = postSearchService.searchPosts(postSearchRequest, page, size);

        return createResponse(HttpStatus.OK, "SEARCH_POSTS_OK", "검색이 성공적으로 완료되었습니다.", result);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Map<String, Object>>> searchPostsByTag(
            @NotBlank String tagName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            HttpSession session
    ){

        log.info("Received searchPostsByTag tagName: {}, page: {}, size: {}", tagName, page, size);
        Map<String, Object> result = postSearchService.searchPostsByTag(tagName, page, size);

        return createResponse(HttpStatus.OK, "SEARCH_POSTS_BY_TAG_OK", "태그 검색이 성공적으로 완료되었습니다.", result);
    }
}
