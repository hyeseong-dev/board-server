package com.example.boardserver.dto.response;

import com.example.boardserver.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private UserDTO userDTO;
}
