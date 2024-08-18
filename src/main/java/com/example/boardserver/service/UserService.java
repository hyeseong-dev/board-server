package com.example.boardserver.service;

import com.example.boardserver.dto.UserDTO;

public interface UserService {
    void register(UserDTO userProfile);
    UserDTO login(String userId, String password);
    boolean isDuplicatedId(String id);
    UserDTO getUserInfo(String id);
    void updatePassword(String id, String beforePassword, String afterPassword);
    void deleteId(String id, String password);
}
