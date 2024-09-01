package com.example.boardserver.exception.handler;

import com.example.boardserver.dto.CommonResponse;
import com.example.boardserver.exception.BoardServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException(RuntimeException e) {
        CommonResponse response = new CommonResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Runtime_Exception",
                e.getMessage(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BoardServerException.class)
    public ResponseEntity<CommonResponse> handleBoardServerException(BoardServerException e) {
        CommonResponse response = new CommonResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "BoardServer_Exception",
                e.getMessage(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e) {
        CommonResponse response = new CommonResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Exception",
                e.getMessage(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
