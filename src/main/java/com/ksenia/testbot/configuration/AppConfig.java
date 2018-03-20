package com.ksenia.testbot.configuration;

import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${messenger4j.pageAccessToken}")
    private String pageAccessToken;
    @Value("${messenger4j.appSecret}")
    private String appSecret;
    @Value("${messenger4j.verifyToken}")
    private String verifyToken;

    @Bean
    public Messenger messengerProducer() {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }
}
