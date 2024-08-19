package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.exception.DuplicatedExeption;
import com.example.boardserver.mapper.CategoryMapper;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.CateogryService;
import com.example.boardserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.boardserver.utils.SHA256Util.encryptSHA256;

@Slf4j
@Service
public class CategoryServiceImpl implements CateogryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        validateCategory(categoryDTO);
        if (accountId == null) {
            log.error("register ERROR: accountId is null");
            throw new RuntimeException("accountId cannot be null");
        }
        categoryMapper.register(categoryDTO);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        validateCategory(categoryDTO);
        categoryMapper.updateCategory(categoryDTO);
    }

    @Override
    public void delete(int categoryId) {
        if (categoryId <= 0) {
            log.error("delete ERROR: invalid categoryId: {}", categoryId);
            throw new RuntimeException("Invalid categoryId");
        }
        categoryMapper.deleteCategory(categoryId);
    }

    private void validateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null || categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            log.error("Category validation failed: {}", categoryDTO);
            throw new RuntimeException("Invalid Category Data");
        }
    }
}
