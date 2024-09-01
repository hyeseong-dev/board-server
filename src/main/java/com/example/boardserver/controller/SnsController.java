package com.example.boardserver.controller;

import com.example.boardserver.config.AWSConfig;
import com.example.boardserver.dto.request.PublishRequestDTO;
import com.example.boardserver.dto.request.SubscribeRequestDTO;
import com.example.boardserver.service.SnsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/sns")
public class SnsController {
     private final SnsService snsService;

     public SnsController(SnsService snsService) {
         this.snsService = snsService;
     }

    @GetMapping("/create-topic")
    public ResponseEntity<String> createTopic(@RequestParam(value="name") String name) {
        try(SnsClient snsClient = snsService.getSnsClient()){
            final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                    .name(name)
                    .build();

            final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

            if(!createTopicResponse.sdkHttpResponse().isSuccessful()) throw getResponseStatusException(createTopicResponse);
            log.info("topic name = {}", createTopicResponse.topicArn());
            return ResponseEntity.ok("TOPIC CREATED");
        } catch(Exception e){
            log.error("Error creating topic {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating topic", e);
        }
     }

     @PostMapping("/subscribe")
     public ResponseEntity<String> subscribe(@RequestBody SubscribeRequestDTO subscribeRequestDto){
         try (SnsClient snsClient = snsService.getSnsClient()) {
             final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                     .protocol("https")
                     .topicArn(subscribeRequestDto.getTopicArn())
                     .endpoint(subscribeRequestDto.getEndpoint())
                     .build();

             final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

             if (!subscribeResponse.sdkHttpResponse().isSuccessful()) {
                 throw getResponseStatusException(subscribeResponse);
             }

             log.info("Subscribed to topic ARN: {}", subscribeResponse.subscriptionArn());
             return ResponseEntity.ok("TOPIC SUBSCRIBED");
         } catch (Exception e) {
             log.error("Error subscribing to topic: {}", e.getMessage());
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error subscribing to topic", e);
         }
     }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody PublishRequestDTO publishRequestDto){
        try (SnsClient snsClient = snsService.getSnsClient()) {
            final PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(publishRequestDto.getTopicArn())
                    .subject("HTTP ENDPOINT TEST MESSAGE")
                    .message(publishRequestDto.getMessage().toString())
                    .build();

            final PublishResponse publishResponse = snsClient.publish(publishRequest);

            if (!publishResponse.sdkHttpResponse().isSuccessful()) {
                throw getResponseStatusException(publishResponse);
            }

            log.info("Message published with status code: {}", publishResponse.sdkHttpResponse().statusCode());
            return ResponseEntity.ok(publishResponse.messageId());
        } catch (Exception e) {
            log.error("Error publishing message: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error publishing message", e);
        }
    }

     private ResponseStatusException getResponseStatusException(SnsResponse snsResponse) {
         return new ResponseStatusException(
                 HttpStatus.INTERNAL_SERVER_ERROR,
                 snsResponse.sdkHttpResponse().statusText().orElse("Error"),
                 new Exception(snsResponse.toString())
         );
     }
}
