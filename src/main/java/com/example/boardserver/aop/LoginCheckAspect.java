package com.example.boardserver.aop;

import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;

@Component
@Aspect
@Log4j2
public class LoginCheckAspect {

    @Autowired
    private HttpServletRequest request; // 현재 HTTP 요청을 주입받는 필드

    @Autowired
    private UserProfileMapper userProfileMapper; // UserProfileMapper를 주입받아 사용

    // @Around 어노테이션으로 LoginCheck 어노테이션이 적용된 메서드의 실행 전후로 이 메서드를 실행
    @Around("@annotation(com.example.boardserver.aop.LoginCheck) && @annotation(loginCheck)")
    public Object authenticateUser(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = request.getSession(false); // 현재 세션을 가져옴 (세션이 없으면 null 반환)

        // 세션이 없거나, 세션에 저장된 사용자 정보가 없는 경우
        if (session == null || (session.getAttribute(SessionUtil.LOGIN_MEMBER_ID) == null
                && session.getAttribute(SessionUtil.LOGIN_ADMIN_ID) == null)) {
            log.error(proceedingJoinPoint.toString() + " - User is not authenticated"); // 인증되지 않은 사용자 로그
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "User is not authenticated") {}; // 401 에러 던짐
        }

        String id = SessionUtil.getLoginMemberId(session); // 세션에서 사용자 ID를 가져옴
        if (id == null) {
            id = SessionUtil.getLoginAdminId(session); // 관리자 ID를 가져옴
        }

        // ID가 세션에 없을 경우
        if (id == null) {
            log.error(proceedingJoinPoint.toString() + " - User ID not found in session"); // 사용자 ID가 없음을 로그로 기록
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "User ID not found in session") {}; // 401 에러 던짐
        }

        UserDTO user = userProfileMapper.getUserProfile(id); // 사용자 정보를 데이터베이스에서 가져옴
        if (user == null) {
            log.error(proceedingJoinPoint.toString() + " - User not found for ID: " + id); // 사용자를 찾을 수 없음을 로그로 기록
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "User not found") {}; // 401 에러 던짐
        }

        // 사용자의 역할(Role) 확인
        if (loginCheck.roles().length > 0 && !Arrays.asList(loginCheck.roles()).contains(user.getStatus().toString())) {
            log.error(proceedingJoinPoint.toString() + " - User does not have required role. User role: " + user.getStatus());
            throw new HttpStatusCodeException(HttpStatus.FORBIDDEN, "User does not have required role") {}; // 403 에러 던짐
        }

        return proceedingJoinPoint.proceed(); // 원래의 메서드를 실행
    }
}
