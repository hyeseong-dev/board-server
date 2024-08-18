package com.example.boardserver.exception;

public class DuplicatedExeption extends RuntimeException {
    public DuplicatedExeption(String msg) {
        super(msg);
    }
}
