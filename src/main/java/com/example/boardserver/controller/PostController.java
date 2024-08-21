package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.service.PostService;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;
    }

    private UserDTO getAuthenticatedUser(HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        if (userId == null) {
            userId = SessionUtil.getLoginAdminId(session);
        }
        return userService.getUserInfo(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Void>> registerPost(
            @RequestBody PostRequest postRequest,
            HttpSession session
    ){
        UserDTO user = getAuthenticatedUser(session);

        // 사용자 정보가 없으면 인증되지 않은 사용자로 처리합니다.
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(HttpStatus.UNAUTHORIZED,
                                        "AUTH_ERROR",
                                    "인증되지 않은 사용자입니다.",
                                null));
        }

        postService.register(user.getId(), postRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new CommonResponse<>(HttpStatus.CREATED,
                                                  "POST_CREATED",
                                                "게시글이 성공적으로 등록되었습니다.",
                                                null));
    }

    @GetMapping("/my-posts")
    @LoginCheck(roles={"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<Map<String, Object>>> myPostInfo(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            HttpSession session
    ){
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "인증되지 않은 사용자입니다.", null));
        }

        List<PostDTO> postDTOList = postService.getMyPostsWithPaging(user.getId(), page, size);
        int totalPages = postService.getTotalPages(user.getId(), size);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", postDTOList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK, "POSTS_RETRIEVED", "내 게시글 목록을 성공적으로 조회했습니다.", response));
    }

    @PatchMapping("/{postId}")
    @LoginCheck(roles={"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<PostDTO>> updatePost(
            @PathVariable(value = "postId") int postId,
            @RequestBody PostRequest postRequest,
            HttpSession session
    ){
        UserDTO user = getAuthenticatedUser(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "인증되지 않은 사용자입니다.", null));
        }

        PostDTO postDTO = postService.updatePost(postId, user.getId(), postRequest);
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK, "POST_UPDATED", "게시글이 성공적으로 수정되었습니다.", postDTO));

    }

    @DeleteMapping("/{postId}")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<?>> deletePost(
            @PathVariable(value = "postId") int postId,
            HttpSession session
    ) {
        UserDTO user = getAuthenticatedUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "인증되지 않은 사용자입니다.", null));
        }

        postService.deletePost(user.getId(), postId);
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK, "POST_DELETED", "게시글이 성공적으로 삭제되었습니다.", null));
    }
}
