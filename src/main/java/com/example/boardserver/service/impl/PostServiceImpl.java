package com.example.boardserver.service.impl;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.PostService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserProfileMapper userProfileMapper;

    public PostServiceImpl(PostMapper postMapper, UserProfileMapper userProfileMapper) {
        this.postMapper = postMapper;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public void validatePostRequest(PostRequest postRequest) {
        if (postRequest == null) {
            throw new IllegalArgumentException("PostRequest cannot be null");
        }
        if (StringUtils.isBlank(postRequest.getName())) {
            throw new IllegalArgumentException("Post name cannot be empty");
        }
        if (StringUtils.isBlank(postRequest.getContents())) {
            throw new IllegalArgumentException("Post contents cannot be empty");
        }
        if (postRequest.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        // 필요에 따라 추가 검증 로직
    }

    private PostDTO convertToPostDTO(int userId, PostRequest postRequest) {
        return PostDTO.builder()
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(userId)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }

    @Transactional
    @Override
    public void register(int userId, PostRequest postRequest) {
        validatePostRequest(postRequest);
        PostDTO postDTO = convertToPostDTO(userId, postRequest);
        postMapper.register(postDTO);
    }

    @Transactional
    @Override
    public PostDTO updatePost(int postId, int userId, PostRequest requestPostDTO) {
        PostDTO existingPost = postMapper.getPost(postId);
        if (existingPost == null) {
            log.error("update ERROR: 해당 게시글 ID 찾을 수 없습니다: {}", postId);
            throw new RuntimeException("updatePost ERROR! 게시글 수정 메서드를 확인해주세요. " + postId);
        }
        if (existingPost.getUserId() != userId) {
            log.error("게시글 수정 실패: 사용자 {}는 게시글 {}를 수정할 권한이 없습니다.", userId, postId);
            throw new RuntimeException("updatePost ERROR! 게시글 수정 메서드를 확인해주세요. " + postId);
        }

        // 부분 업데이트 로직
        if (requestPostDTO.getName() != null) {
            existingPost.setName(requestPostDTO.getName());
        }
        if (requestPostDTO.getContents() != null) {
            existingPost.setContents(requestPostDTO.getContents());
        }

        if (requestPostDTO.getCategoryId() != 0) {
            existingPost.setCategoryId(requestPostDTO.getCategoryId());
        }
        PostDTO updatedPost = null;
        updatedPost = convertToPostDTO(userId, requestPostDTO);
        updatedPost.setId(postId);
        updatedPost.setUpdateTime(new Date());

        try {
            postMapper.updatePost(existingPost);
        } catch (Exception e) {
            log.error("ID {}인 게시글 수정 중 오류 발생. 오류 내용: {}", postId, e.getMessage(), e);
            throw new RuntimeException("게시글 수정에 실패했습니다. 나중에 다시 시도해 주세요.", e);
        }
        return postMapper.getPost(postId);
    }


    @Override
    public PostDTO getPost(int postId) {
        return postMapper.getPost(postId);
    }

    @Transactional
    @Override
    public void deletePost ( int userId, int postId){
        if (userId <= 0 && postId <= 0) {
            log.error("delete ERROR: invalid postId {} or userId: {}", postId, userId);
            throw new RuntimeException("deletePost ERROR! 게시글 삭제 메서드를 확인해주세요. " + postId);
        }
        postMapper.deletePost(postId);
    }

    @Transactional
    @Override
    public List<PostDTO> getMyPostsWithPaging(int userId, int page, int size) {
        int offset = (page - 1) * size;
        return postMapper.selectMyPostsWithPaging(userId, offset, size);
    }

    @Override
    public int getTotalPages(int userId, int size) {
        int totalPosts = postMapper.countMyPosts(userId);
        return (int) Math.ceil((double) totalPosts / size);
    }
}
