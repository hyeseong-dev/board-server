package com.example.boardserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {  // 제네릭 타입 T를 사용
    private HttpStatus status;   // HTTP 상태 코드
    private String code;         // 응답 코드 (예: "SUCCESS", "ERROR" 등)
    private String message;      // 응답 메시지
    private T responseBody;      // 실제 응답 데이터 (제네릭 타입 T)
}