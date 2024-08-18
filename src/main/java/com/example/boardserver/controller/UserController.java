package com.example.boardserver.controller;

import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.request.UserDeleteId;
import com.example.boardserver.dto.request.UserLoginRequest;
import com.example.boardserver.dto.request.UserUpdatePasswordRequest;
import com.example.boardserver.dto.response.LoginResponse;
import com.example.boardserver.dto.response.UserInfoResponse;
import com.example.boardserver.service.UserService;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO) {
        if(UserDTO.hasNullDataBeforeRegister(userDTO)){
            throw new RuntimeException("회원 가입 정보를 확인해주세요");
        }
        userService.register(userDTO);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponse> login(
            @RequestBody UserLoginRequest loginRequest,
            HttpSession session
     ) {
        try {
            String userId = loginRequest.getUserId();
            String password = loginRequest.getPassword();
            UserDTO userInfo = userService.login(userId, password);
            String id = String.valueOf(userInfo.getId());

            if (userInfo == null) {
                return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.NOT_FOUND);
            }

            LoginResponse loginResponse = LoginResponse.success(userInfo);
            if (userInfo.getStatus() == UserDTO.Status.ADMIN) {
                SessionUtil.setLoginAdminId(session, id);
            } else {
                SessionUtil.setLoginMemberId(session, id);
            }

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("Login error: ", e);
            return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserInfoResponse> memberInfo(HttpSession session){
        String id = SessionUtil.getLoginMemberId(session);
        if(id == null) id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        if (memberInfo == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new UserInfoResponse(memberInfo));
     }

     @PutMapping("/logout")
    public void logout(HttpSession session){
        SessionUtil.clear(session);
     }

     @PatchMapping("/password")
    public ResponseEntity<LoginResponse> changePassword(
            @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
            HttpSession session
     ){
         String id = SessionUtil.getLoginMemberId(session);
         if (id == null) {
             return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.UNAUTHORIZED);
         }

         String beforePassword = userUpdatePasswordRequest.getBeforePassword();
         String afterPassword = userUpdatePasswordRequest.getAfterPassword();

         try {
             UserDTO memberInfo = userService.getUserInfo(id);
             userService.updatePassword(id, beforePassword, afterPassword);
             UserDTO userInfo = userService.login(memberInfo.getUserId(), afterPassword);
             if(userInfo == null){
                 return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.BAD_REQUEST);
             }
             LoginResponse loginResponse = LoginResponse.success(userInfo);
             return ResponseEntity.ok(loginResponse);
         } catch (IllegalArgumentException e) {
             log.error("Password update failed for user: {}", id, e);
             return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.BAD_REQUEST);
         }

     }

     @DeleteMapping("")
    public ResponseEntity<LoginResponse> deleteId(
             @RequestBody UserLoginRequest requestBody,
            HttpSession session
     ){
         String id = SessionUtil.getLoginMemberId(session);
         if (id == null) {
             return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.UNAUTHORIZED);
         }

         try {
             userService.deleteId(id, requestBody.getPassword());
             return ResponseEntity.ok(LoginResponse.success(null));
         } catch (RuntimeException e) {
             log.error("Delete failed for user: {}", id, e);
             return new ResponseEntity<>(LoginResponse.getFailResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
}
