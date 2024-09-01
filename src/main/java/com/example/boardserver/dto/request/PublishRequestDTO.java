package com.example.boardserver.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PublishRequestDTO {
    private String topicArn;
    private Map<String, Object> message;
}
