package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.*;
import com.example.boardserver.dto.request.CommentRequest;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.dto.request.TagRequest;
import com.example.boardserver.service.PostService;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    private ResponseEntity<CommonResponse<Object>> createResponse(HttpStatus status, String code, String message, Object data) {
        return ResponseEntity.status(status)
                .body(new CommonResponse<>(status, code, message, data));
    }

    private UserDTO getAuthenticatedUser(HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        if (userId == null) {
            userId = SessionUtil.getLoginAdminId(session);
        }
        return userService.getUserInfo(userId);
    }

    private ResponseEntity<CommonResponse<Object>> handleUnauthorized() {
        return createResponse(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "인증되지 않은 사용자입니다.", null);
    }

    @PostMapping
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> registerPost(@RequestBody @Valid PostRequest postRequest, HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        PostDTO result = postService.register(user.getId(), postRequest);
        return createResponse(HttpStatus.CREATED, "POST_CREATED", "게시글이 성공적으로 등록되었습니다.", result);
    }

    @GetMapping("/my-posts")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> myPostInfo(
            @RequestParam(value = "page", defaultValue = "1") Long page,
            @RequestParam(value = "size", defaultValue = "10") Long size,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        Map<String, Object> result = postService.getMyPosts(user.getId(), page, size);
        return createResponse(HttpStatus.OK, "POSTS_RETRIEVED", "내 게시글 목록을 성공적으로 조회했습니다.", result);
    }

    @PatchMapping("/{postId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> updatePost(
            @PathVariable(value = "postId") Long postId,
            @RequestBody @Valid PostRequest postRequest,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        PostDTO postDTO = postService.updatePost(postId, user.getId(), postRequest);
        return createResponse(HttpStatus.OK, "POST_UPDATED", "게시글이 성공적으로 수정되었습니다.", postDTO);
    }

    @DeleteMapping("/{postId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> deletePost(
            @PathVariable(value = "postId") Long postId,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        postService.deletePost(user.getId(), postId);
        return createResponse(HttpStatus.OK, "POST_DELETED", "게시글이 성공적으로 삭제되었습니다.", null);
    }

    @PostMapping("/comments")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> registerComment(
            @RequestBody @Valid CommentRequest commentRequest,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        CommentDTO result = postService.registerComment(user.getId(), commentRequest);
        return createResponse(HttpStatus.CREATED, "COMMENT_CREATED", "댓글이 성공적으로 등록되었습니다.", result);
    }

    @PatchMapping("/comments/{commentId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> updateComment(
            @PathVariable(value = "commentId") Long commentId,
            @RequestBody @Valid CommentRequest commentRequest,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        postService.updateComment(user.getId(), commentId, commentRequest);
        return createResponse(HttpStatus.OK, "COMMENT_UPDATED", "댓글이 성공적으로 수정되었습니다.", null);
    }

    @DeleteMapping("/comments/{commentId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> deleteComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        postService.deleteComment(user.getId(), commentId);
        return createResponse(HttpStatus.OK, "COMMENT_DELETED", "댓글이 성공적으로 삭제되었습니다.", null);
    }

    @PostMapping("/tags")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> registerTag(
            @RequestBody @Valid TagRequest tagRequest,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        postService.registerTag(user.getId(), tagRequest);
        return createResponse(HttpStatus.CREATED, "TAG_CREATED", "태그가 성공적으로 등록되었습니다.", null);
    }

    @PatchMapping("/tags/{tagId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> updateTag(
            @PathVariable(value = "tagId") Long tagId,
            @RequestBody @Valid TagRequest tagRequest,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        postService.updateTag(user.getId(), tagId, tagRequest);
        return createResponse(HttpStatus.NO_CONTENT, "TAG_UPDATED", "태그가 성공적으로 수정되었습니다.", null);
    }

    @DeleteMapping("/tags/{tagId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Object>> deleteTag(
            @PathVariable(value = "tagId") Long tagId,
            HttpSession session) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return handleUnauthorized();
        }
        postService.deleteTag(user.getId(), tagId);
        return createResponse(HttpStatus.NO_CONTENT, "TAG_DELETED", "태그가 성공적으로 삭제되었습니다.", null);
    }
}