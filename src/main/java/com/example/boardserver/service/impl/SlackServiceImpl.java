package com.example.boardserver.service.impl;

import com.example.boardserver.service.SlackService;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SlackServiceImpl implements SlackService {
    @Value("${slack.token}")
    String slackToken;

    @Value("${slack.webhook.url}")
    String slackWebhookUrl;

    @Override
    public void sendSlackMessageByBot(String message, String channel) {
        try {
            MethodsClient methodsClient = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message)
                    .build();
            ChatPostMessageResponse response = methodsClient.chatPostMessage(request);

            if (!response.isOk()) {
                log.error("Slack API Error: {}", response.getError());
            } else {
                log.info("Message sent to channel: {}", channel);
            }
        } catch (SlackApiException | IOException e) {
            log.error("Error sending Slack message to channel {}: {}", channel, e.getMessage());
        }
    }

    @Override
    public void sendSlackMessageByWebhook(String message, String channel) {
        try {
            Slack slack = Slack.getInstance();
            slack.send(slackWebhookUrl, message);  // Webhook URL을 통해 메시지 전송
            log.info("Message sent via webhook to channel: {}", channel);
        } catch (IOException e) {
            log.error("Error sending message via webhook: {}", e.getMessage());
        }
    }
}