package com.example.boardserver.service.impl;

import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.exception.DuplicatedExeption;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.boardserver.utils.SHA256Util.encryptSHA256;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public void register(UserDTO userProfile) {
        boolean dupleResult = isDuplicatedId(userProfile.getUserId());
        if(dupleResult){
            throw new DuplicatedExeption("중복된 아이디입니다.");
        }
        userProfile.setCreateTime(new Date());
        userProfile.setPassword(encryptSHA256(userProfile.getPassword()));
    int insertCount = userProfileMapper.register(userProfile);

    if(insertCount != 1){
        log.error("insertMember ERROR! {}", userProfile);
        throw new RuntimeException("insertUser ERROR 회원가입 메서드를 확인해주세요|n" + "Param : " + userProfile);
    }
    }

    @Override
    public UserDTO login(String userId, String password) {
        String cryptoPassword  = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByUserIdAndPassword(userId, cryptoPassword);
        return memberInfo;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        int result = userProfileMapper.idCheck(id);
        return result == 1;
    }

    @Override
    public UserDTO getUserInfo(String id) {
        return userProfileMapper.getUserProfile(id);
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptoPassword  = encryptSHA256(beforePassword);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);
        if(memberInfo != null){
            memberInfo.setPassword(encryptSHA256(afterPassword));
            int insertCount = userProfileMapper.updatePassword(memberInfo);
        } else{
            log.error("updatePassword ERROR! {}", memberInfo);
            throw new IllegalArgumentException("updatePasswrod ERROR! 비밀번호 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cryptoPassword  = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if(memberInfo != null){

            userProfileMapper.deleteUserProfile(memberInfo.getUserId());
        } else{
            log.error("deleteId ERROR! {}", memberInfo);
            throw new IllegalArgumentException("deleteId ERROR! 삭제 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }
}
