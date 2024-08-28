package com.example.boardserver.dto;

import com.example.boardserver.dto.request.CommentRequest;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDTO implements Serializable {
    private Long id;
    private Long postId;
    private String contents;
    private Long subCommentId;

    public static CommentDTO fromRequest(Long userId, CommentRequest commentRequest) {
        return CommentDTO.builder()
                .postId(commentRequest.getPostId())
                .contents(commentRequest.getContents())
                .subCommentId(commentRequest.getSubCommentId())
                .build();
    }
}
