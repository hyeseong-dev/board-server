package com.example.boardserver.service;

import com.example.boardserver.dto.UserDTO;

public interface UserService {
    void register(UserDTO userProfile);
    UserDTO login(String userId, String password);
    boolean isDuplicatedId(String id);
    UserDTO getUserInfo(String id);
    void updatePassword(Long id, String beforePassword, String afterPassword);
    void deleteId(Long id, String password);
}
