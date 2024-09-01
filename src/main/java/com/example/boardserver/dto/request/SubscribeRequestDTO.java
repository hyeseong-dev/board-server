package com.example.boardserver.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SubscribeRequestDTO {
    private String topicArn;
    private String endpoint;
}
