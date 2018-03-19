package com.ksenia.testbot.service;

import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessengerProducer {
    @Value("${messenger4j.pageAccessToken}")
    private String pageAccessToken;
    @Value("${messenger4j.appSecret}")
    private String appSecret;
    @Value("${messenger4j.verifyToken}")
    private String verifyToken;


    @Autowired
    public Messenger messengerProducer() {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }
}
