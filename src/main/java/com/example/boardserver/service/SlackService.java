package com.example.boardserver.service;

import org.springframework.stereotype.Service;

@Service
public interface SlackService {

    void sendSlackMessageByBot(String message, String channel);
    void sendSlackMessageByWebhook(String message, String channel);
}
