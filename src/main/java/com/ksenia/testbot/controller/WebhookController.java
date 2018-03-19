package com.ksenia.testbot.controller;


import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;

import com.ksenia.testbot.service.EventHandler;
import com.ksenia.testbot.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import static java.util.Optional.of;


@RestController
public class WebhookController {


    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);
    private Messenger messenger;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    WebhookController(@Value("${messenger4j.pageAccessToken}") final String pageAccessToken,
                      @Value("${messenger4j.appSecret}") final String appSecret,
                      @Value("${messenger4j.verifyToken}") final String verifyToken) {
        messenger = Messenger.create(pageAccessToken, appSecret, verifyToken);
    }


    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.verify_token") String verifyTokenInput,
                                                @RequestParam("hub.challenge") String challenge) {

        LOGGER.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyTokenInput, challenge);

        try {
            messenger.verifyWebhook(mode, verifyTokenInput);
        } catch (MessengerVerificationException e) {
            LOGGER.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        return ResponseEntity.status(HttpStatus.OK).body(challenge);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void verifyWebhook(@RequestBody String payload, @RequestHeader("X-Hub-Signature") final String signature) {

        System.out.println(payload);
        try {
            messenger.onReceiveEvents(payload, of(signature), event -> {
                try {
                    eventHandler.handle(event, messenger);
                } catch (MessengerApiException | MessengerIOException e) {
                    LOGGER.warn("Processing of callback payload failed: {}", e.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.warn("Processing of callback payload failed: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void setUp(){
        try {
            messenger.updateSettings(ResponseMessage.greetingText());
            messenger.updateSettings(ResponseMessage.startedButton());
            messenger.updateSettings(ResponseMessage.persistentMenu());
        } catch (MessengerApiException | MessengerIOException e) {
            LOGGER.warn("Cannot update settings: {}", e.getMessage());
        }


    }
}