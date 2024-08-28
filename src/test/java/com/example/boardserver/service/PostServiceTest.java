package com.example.boardserver.service;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 게시글_등록_성공() {
        // Given
        Long userId = 1L;
        PostRequest postRequest = new PostRequest();
        postRequest.setName("테스트 게시글");
        postRequest.setContents("테스트 내용");
        postRequest.setCategoryId(1);

        when(postMapper.register(any(PostDTO.class))).thenReturn(1L);

        // When
        assertDoesNotThrow(() -> postService.register(userId, postRequest));

        // Then
        verify(postMapper, times(1)).register(any(PostDTO.class));
    }

    @Test
    void 게시글_수정_성공() {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        PostRequest postRequest = new PostRequest();
        postRequest.setName("수정된 게시글");
        postRequest.setContents("수정된 내용");

        PostDTO existingPost = new PostDTO();
        existingPost.setId(postId);
        existingPost.setUserId(userId);

        when(postMapper.getPost(postId)).thenReturn(existingPost);
        doNothing().when(postMapper).updatePost(any(PostDTO.class));

        // When
        PostDTO result = postService.updatePost(postId, userId, postRequest);

        // Then
        assertNotNull(result);
        assertEquals("수정된 게시글", result.getName());
        verify(postMapper, times(1)).updatePost(any(PostDTO.class));
    }

    @Test
    void 내_게시글_페이징_조회_성공() {
        // Given
        Long userId = 1L;
        Long page = 1L;
        Long size = 10L;
        List<PostDTO> expectedPosts = Arrays.asList(new PostDTO(), new PostDTO());

        when(postMapper.selectMyPostsWithPaging(anyLong(), anyLong(), anyLong())).thenReturn(expectedPosts);

        // When
        List<PostDTO> result = postService.getMyPostsWithPaging(userId, page, size);

        // Then
        assertEquals(expectedPosts.size(), result.size());
        verify(postMapper, times(1)).selectMyPostsWithPaging(userId, (page - 1) * size, size);
    }

    @Test
    void 총_페이지_수_계산_성공() {
        // Given
        Long userId = 1L;
        Long size = 10L;
        Long totalPosts = 25L;

        when(postMapper.countMyPosts(userId)).thenReturn(totalPosts);

        // When
        Long result = postService.getTotalPages(userId, size);

        // Then
        assertEquals(3, result); // 25개 게시글 / 페이지당 10개 = 3 페이지
        verify(postMapper, times(1)).countMyPosts(userId);
    }

    @Test
    void 특정_게시글_조회_성공() {
        // Given
        Long postId = 1L;
        PostDTO expectedPost = new PostDTO();
        expectedPost.setId(postId);

        when(postMapper.getPost(postId)).thenReturn(expectedPost);

        // When
        PostDTO result = postService.getPost(postId);

        // Then
        assertNotNull(result);
        assertEquals(postId, result.getId());
        verify(postMapper, times(1)).getPost(postId);
    }

    @Test
    void 게시글_삭제_성공() {
        // Given
        Long userId = 1L;
        Long postId = 1L;

        doNothing().when(postMapper).deletePost(postId);

        // When
        assertDoesNotThrow(() -> postService.deletePost(userId, postId));

        // Then
        verify(postMapper, times(1)).deletePost(postId);
    }

    @Test
    void 게시글_요청_유효성_검사() {
        // Given
        PostRequest validRequest = new PostRequest();
        validRequest.setName("유효한 이름");
        validRequest.setContents("유효한 내용");
        validRequest.setCategoryId(1);

        PostRequest invalidRequest = new PostRequest();

        // When & Then
        assertDoesNotThrow(() -> postService.validatePostRequest(validRequest));
        assertThrows(IllegalArgumentException.class, () -> postService.validatePostRequest(invalidRequest));
    }
}