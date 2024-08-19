package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.request.CategoryRequest;
import com.example.boardserver.dto.response.UserInfoResponse;
import com.example.boardserver.service.CateogryService;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CateogryService categoryService;
    private UserService userService;
    public CategoryController(
            CateogryService categoryService,
            UserService userService
    ) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(roles = {"ADMIN"})
    public ResponseEntity<Object> registerCategory(
            @RequestBody CategoryDTO categoryDTO, HttpSession session
    ) {
        String id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        categoryService.register(memberInfo.getUserId(), categoryDTO);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("{categoryId}")
    @LoginCheck(roles= {"ADMIN"})
    public ResponseEntity<Object> updateCategory(
        @PathVariable(value = "categoryId") int categoryId,
        @RequestBody CategoryRequest categoryRequest,
        HttpSession session
    ) {
        String id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), CategoryDTO.SortStatus.NEWEST, 10, 1);
        categoryService.update(categoryDTO);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("{categoryId}")
    @LoginCheck(roles= {"ADMIN"})
    public ResponseEntity<Object> deleteCategory(
            @PathVariable(value = "categoryId") int categoryId,
            HttpSession session
    ) {
        String id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        categoryService.delete(categoryId);
        return ResponseEntity.ok(null);
    }
}
