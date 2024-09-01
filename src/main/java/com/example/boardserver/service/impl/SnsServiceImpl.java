package com.example.boardserver.service.impl;

import com.example.boardserver.config.AWSConfig;
import com.example.boardserver.service.SnsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Slf4j
@Service
public class SnsServiceImpl implements SnsService {
    AWSConfig awsConfig;

    public SnsServiceImpl(AWSConfig awsConfig){
        this.awsConfig = awsConfig;
    }

    @Override
    public AwsCredentialsProvider getAWSCredentials(String accessKeyId, String secretAccessKey) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return () -> awsBasicCredentials;
    }

    @Override
    public SnsClient getSnsClient(){
        return SnsClient.builder()
                .credentialsProvider(
                        getAWSCredentials(awsConfig.getAccessKey(), awsConfig.getAwsSecretKey()))
                .region(Region.of(awsConfig.getAwsRegion()))
                .build();
    }
}
