package com.example.boardserver.mapper;

import com.example.boardserver.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    public UserDTO getUserProfile(@Param("id") String id);

    int insertUserProfile(
            @Param("id") String id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("address") String address
    );

    int updateUserProfile(
            @Param("id") String id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("address") String address
    );

    void deleteUserProfile(@Param("userId") String userId);

    int register(UserDTO userDTO);

    UserDTO findByIdAndPassword(
            @Param("id") String id,
            @Param("password") String password
    );

    UserDTO findByUserIdAndPassword(
            @Param("userId") String userId,
            @Param("password") String password
    );
    int idCheck(String id);

    int updatePassword(UserDTO userDTO);
    int updateAddress(UserDTO userDTO);
}
