package com.example.boardserver.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.sns.SnsClient;

public interface SnsService {
    AwsCredentialsProvider getAWSCredentials(String accessKeyId, String secretAccessKey);
    SnsClient getSnsClient();

    }
