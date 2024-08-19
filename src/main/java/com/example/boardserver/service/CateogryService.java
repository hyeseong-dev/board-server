package com.example.boardserver.service;

import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.UserDTO;

public interface CateogryService {
    void register(String accountId, CategoryDTO categoryDTO);
    void update(CategoryDTO categoryDTO);
    void delete(int categoryId);
}
