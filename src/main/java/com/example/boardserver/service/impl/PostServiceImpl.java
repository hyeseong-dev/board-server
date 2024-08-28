package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
import com.example.boardserver.dto.request.CommentRequest;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.dto.request.TagRequest;
import com.example.boardserver.mapper.CommentMapper;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.mapper.TagMapper;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.PostService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final UserProfileMapper userProfileMapper;

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
    }

    @Override
    public CommentDTO registerComment(Long userId, CommentRequest commentRequest) {
        try {
            CommentDTO commentDto = CommentDTO.fromRequest(userId, commentRequest);
            commentMapper.register(commentDto);
            return commentDto;
        } catch (Exception e) {
            log.error("Error registering comment: {}", commentRequest, e);
            throw new RuntimeException("Failed to register comment", e);
        }
    }

    @Override
    public void updateComment(Long userId, Long commentId, CommentRequest commentRequest) {
        try {
            CommentDTO commentDTO = CommentDTO.fromRequest(userId, commentRequest);
            commentMapper.updateComment(commentDTO, commentId);
        } catch (Exception e) {
            log.error("Error updating comment: {}", commentRequest, e);
            throw new RuntimeException("Failed to update comment", e);
        }
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        try {
            commentMapper.deleteComment(commentId);
        } catch (Exception e) {
            log.error("Error deleting comment: commentId={}", commentId, e);
            throw new RuntimeException("Failed to delete comment", e);
        }
    }

    @Override
    public void registerTag(Long userId, TagRequest tagRequest) {
        try {
            TagDTO existingTag = tagMapper.findTagByName(tagRequest.getName());
            if (existingTag == null) {
                TagDTO tagDTO = TagDTO.fromRequestForCreate(tagRequest);
                tagMapper.register(tagDTO);
            } else {
                log.info("Tag with name {} already exists", tagRequest.getName());
            }
        } catch (Exception e) {
            log.error("Error registering tag: {}", tagRequest, e);
            throw new RuntimeException("Failed to register tag", e);
        }
    }

    @Override
    @Transactional
    public void updateTag(Long userId, Long tagId, TagRequest tagRequest) {
        try {
            TagDTO tagDTO = TagDTO.fromRequestForUpdate(tagId, tagRequest);
            Long updatedRow = tagMapper.updateTag(tagId, tagDTO);
            if (updatedRow == 0) {
                throw new RuntimeException("Tag not found with id: " + tagId);
            }
        } catch (Exception e) {
            log.error("Error updating tag: {}", tagRequest, e);
            throw new RuntimeException("Failed to update tag", e);
        }
    }

    @Override
    public void deleteTag(Long userId, Long tagId) {
        try {
            Long deletedRow = tagMapper.deleteTag(tagId);
            if (deletedRow == 0) {
                throw new RuntimeException("Tag not found with id: " + tagId);
            }
        } catch (Exception e) {
            log.error("Error deleting tag: tagId={}", tagId, e);
            throw new RuntimeException("Failed to delete tag", e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "getProducts", allEntries = true)
    public PostDTO register(Long userId, PostRequest postRequest) {
        validatePostRequest(postRequest);
        PostDTO postDTO = PostDTO.fromRequest(userId, postRequest);
        postMapper.register(postDTO);
        processTagsForPost(postDTO);
        return postDTO;
    }

    private void processTagsForPost(PostDTO postDTO) {
        if (postDTO.getTagList() != null && !postDTO.getTagList().isEmpty()) {
            for (TagDTO tagDTO : postDTO.getTagList()) {
                TagDTO existingTag = tagMapper.findTagByName(tagDTO.getName());
                if (existingTag == null) {
                    tagMapper.register(tagDTO);
                    existingTag = tagDTO;
                } else {
                    tagDTO.setId(existingTag.getId());
                }
                tagDTO.setPostId(postDTO.getId());
                tagMapper.createPostTag(existingTag.getId(), postDTO.getId());
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "getProducts", allEntries = true)
    public PostDTO updatePost(Long postId, Long userId, PostRequest requestPostDTO) {
        PostDTO existingPost = getAndValidatePost(postId, userId);
        updatePostFields(existingPost, requestPostDTO);
        try {
            postMapper.updatePost(existingPost);
        } catch (Exception e) {
            log.error("Error updating post: postId={}", postId, e);
            throw new RuntimeException("Failed to update post", e);
        }
        return postMapper.getPost(postId);
    }

    private PostDTO getAndValidatePost(Long postId, Long userId) {
        PostDTO existingPost = postMapper.getPost(postId);
        if (existingPost == null) {
            throw new RuntimeException("Post not found with id: " + postId);
        }
        if (!existingPost.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to update this post");
        }
        return existingPost;
    }

    private void updatePostFields(PostDTO existingPost, PostRequest requestPostDTO) {
        if (requestPostDTO.getName() != null) {
            existingPost.setName(requestPostDTO.getName());
        }
        if (requestPostDTO.getContents() != null) {
            existingPost.setContents(requestPostDTO.getContents());
        }
        if (requestPostDTO.getCategoryId() != null && requestPostDTO.getCategoryId() != 0) {
            existingPost.setCategoryId(requestPostDTO.getCategoryId());
        }
        existingPost.setUpdateTime(new Date());
    }

    @Override
    public PostDTO getPost(Long postId) {
        PostDTO post = postMapper.getPost(postId);
        if (post == null) {
            throw new RuntimeException("Post not found with id: " + postId);
        }
        return post;
    }

    @Override
    @Transactional
    @CacheEvict(value = "getProducts", allEntries = true)
    public void deletePost(Long userId, Long postId) {
        if (userId <= 0 || postId <= 0) {
            throw new IllegalArgumentException("Invalid userId or postId");
        }

        try {
            // 게시글 존재 여부 확인
            PostDTO post = postMapper.getPost(postId);
            if (post == null) {
                throw new RuntimeException("Post not found with id: " + postId);
            }


            postMapper.deletePostTagByPostId(postId); //  게시글과 관련된 posttag 레코드를 삭제
            commentMapper.deleteCommentsByPostId(postId); // 게시글과 관련된 comment 레코드를 삭제

            // 그 다음 게시글 삭제
            postMapper.deletePost(postId);

            log.info("Successfully deleted post with id: {}", postId);
        } catch (DataAccessException e) {
            log.error("Database error occurred while deleting post: postId={}", postId, e);
            throw new RuntimeException("Database error occurred while deleting post", e);
        } catch (Exception e) {
            log.error("Error deleting post: postId={}", postId, e);
            throw new RuntimeException("Failed to delete post", e);
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getMyPosts(Long userId, Long page, Long size) {
        Long offset = (page - 1) * size;
        List<PostDTO> posts = postMapper.selectMyPostsWithPaging(userId, offset, size);
        Long totalCount = postMapper.countMyPosts(userId);
        Long totalPages = (long) Math.ceil((double) totalCount / size);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);

        log.info("Search result: posts size = {}, totalCount = {}, totalPages = {}",
                posts.size(), totalCount, totalPages);

        return result;
    }

    @Override
    public Long getTotalPages(Long userId, Long size) {
        Long totalPosts = postMapper.countMyPosts(userId);
        return (long) Math.ceil((double) totalPosts / size);
    }
}