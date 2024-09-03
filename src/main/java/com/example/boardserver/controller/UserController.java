package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.request.UserLoginRequest;
import com.example.boardserver.dto.request.UserUpdatePasswordRequest;
import com.example.boardserver.dto.response.LoginResponse;
import com.example.boardserver.dto.response.UserInfoResponse;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 공통 응답 생성 메서드
    private <T> ResponseEntity<CommonResponse<T>> createResponse(HttpStatus status, String code, String message, T data) {
        return ResponseEntity.status(status)
                .body(new CommonResponse<>(status, code, message, data));
    }

    // 인증된 사용자 정보 가져오기
    private UserDTO getAuthenticatedUser(HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        if (userId == null) {
            userId = SessionUtil.getLoginAdminId(session);
        }
        return userService.getUserInfo(userId);
    }

    // 인증되지 않은 사용자에 대한 처리
    private <T> ResponseEntity<CommonResponse<T>> handleUnauthorized() {
        return createResponse(HttpStatus.UNAUTHORIZED, "AUTH_ERROR", "인증되지 않은 사용자입니다.", null);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommonResponse<Void>> signUp(@RequestBody UserDTO userDTO) {
        if (UserDTO.hasNullDataBeforeRegister(userDTO)) {
            throw new RuntimeException("회원 가입 정보를 확인해주세요");
        }
        userService.register(userDTO);
        return createResponse(HttpStatus.CREATED, "USER_REGISTERED", "회원 가입이 성공적으로 완료되었습니다.", null);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse<LoginResponse>> login(
            @RequestBody UserLoginRequest loginRequest,
            HttpSession session
    ) {
        try {
            String userId = loginRequest.getUserId();
            String password = loginRequest.getPassword();
            UserDTO userInfo = userService.login(userId, password);

            if (userInfo == null) {
                return createResponse(HttpStatus.NOT_FOUND, "LOGIN_FAILED", "아이디 또는 비밀번호가 잘못되었습니다.", LoginResponse.getFailResponse());
            }

            String id = String.valueOf(userInfo.getId());
            if (userInfo.getStatus() == UserDTO.Status.ADMIN) {
                SessionUtil.setLoginAdminId(session, id);
            } else {
                SessionUtil.setLoginMemberId(session, id);
            }

            return createResponse(HttpStatus.OK, "LOGIN_SUCCESS", "로그인이 성공적으로 완료되었습니다.", LoginResponse.success(userInfo));

        } catch (RuntimeException e) {
            log.error("Login error: ", e);
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "LOGIN_ERROR", "로그인 중 오류가 발생했습니다.", LoginResponse.getFailResponse());
        }
    }

    @GetMapping("/my-info")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<UserInfoResponse>> memberInfo(HttpSession session) {
        UserDTO memberInfo = getAuthenticatedUser(session);
        if (memberInfo == null) {
            return handleUnauthorized();
        }
        return createResponse(HttpStatus.OK, "USER_INFO_RETRIEVED", "회원 정보 조회가 성공적으로 완료되었습니다.", new UserInfoResponse(memberInfo));
    }

    @PutMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(HttpSession session) {
        SessionUtil.clear(session);
        return createResponse(HttpStatus.OK, "LOGOUT_SUCCESS", "로그아웃이 성공적으로 완료되었습니다.", null);
    }

    @PatchMapping("/password")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<LoginResponse>> changePassword(
            @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
            HttpSession session
    ) {
        UserDTO memberInfo = getAuthenticatedUser(session);
        if (memberInfo == null) {
            return handleUnauthorized();
        }

        ;
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(memberInfo.getId(), beforePassword, afterPassword);
            UserDTO userInfo = userService.login(memberInfo.getUserId(), afterPassword);
            if (userInfo == null) {
                return createResponse(HttpStatus.BAD_REQUEST, "PASSWORD_CHANGE_FAILED", "비밀번호 변경에 실패했습니다.", LoginResponse.getFailResponse());
            }
            return createResponse(HttpStatus.OK, "PASSWORD_CHANGED", "비밀번호가 성공적으로 변경되었습니다.", LoginResponse.success(userInfo));
        } catch (IllegalArgumentException e) {
            log.error("Password update failed for user: {}", memberInfo.getId(), e);
            return createResponse(HttpStatus.BAD_REQUEST, "PASSWORD_CHANGE_ERROR", "비밀번호 변경 중 오류가 발생했습니다.", LoginResponse.getFailResponse());
        }
    }

    @DeleteMapping("")
    @LoginCheck(roles = {"DEFAULT", "ADMIN"})
    public ResponseEntity<CommonResponse<LoginResponse>> deleteId(
            @RequestBody UserLoginRequest requestBody,
            HttpSession session
    ) {
        UserDTO memberInfo = getAuthenticatedUser(session);
        if (memberInfo == null) {
            return handleUnauthorized();
        }

        try {
            userService.deleteId(memberInfo.getId(), requestBody.getPassword());
            return createResponse(HttpStatus.OK, "USER_DELETED", "회원 탈퇴가 성공적으로 완료되었습니다.", LoginResponse.success(null));
        } catch (RuntimeException e) {
            log.error("Delete failed for user: {}", memberInfo.getId(), e);
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "USER_DELETE_ERROR", "회원 탈퇴 중 오류가 발생했습니다.", LoginResponse.getFailResponse());
        }
    }
}
