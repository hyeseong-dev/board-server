package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.request.CategoryRequest;
import com.example.boardserver.service.CateogryService;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CateogryService categoryService;
    private final UserService userService;

    private <T> ResponseEntity<CommonResponse<T>> createResponse(HttpStatus status, String code, String message, T data) {
        return ResponseEntity.status(status)
                .body(new CommonResponse<>(status, code, message, data));
    }

    private ResponseEntity<CommonResponse<Void>> handleUnauthorized() {
        return createResponse(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "인증되지 않은 사용자입니다.", null);
    }

    @PostMapping()
    @LoginCheck(roles = {"ADMIN"})
    public ResponseEntity<CommonResponse<Void>> registerCategory(
            @RequestBody CategoryDTO categoryDTO, HttpSession session) {
        String id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return handleUnauthorized();
        }
        categoryService.register(memberInfo.getUserId(), categoryDTO);
        return createResponse(HttpStatus.CREATED, "CATEGORY_CREATED", "카테고리가 성공적으로 등록되었습니다.", null);
    }

    @PatchMapping("{categoryId}")
    @LoginCheck(roles = {"ADMIN"})
    public ResponseEntity<CommonResponse<Void>> updateCategory(
            @PathVariable(value = "categoryId") int categoryId,
            @RequestBody CategoryRequest categoryRequest,
            HttpSession session) {
        String id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return handleUnauthorized();
        }
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), CategoryDTO.SortStatus.NEWEST, 10, 1);
        categoryService.update(categoryDTO);
        return createResponse(HttpStatus.OK, "CATEGORY_UPDATED", "카테고리가 성공적으로 수정되었습니다.", null);
    }

    @DeleteMapping("{categoryId}")
    @LoginCheck(roles = {"ADMIN"})
    public ResponseEntity<CommonResponse<Void>> deleteCategory(
            @PathVariable(value = "categoryId") int categoryId,
            HttpSession session) {
        String id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return handleUnauthorized();
        }
        categoryService.delete(categoryId);
        return createResponse(HttpStatus.OK, "CATEGORY_DELETED", "카테고리가 성공적으로 삭제되었습니다.", null);
    }
}
