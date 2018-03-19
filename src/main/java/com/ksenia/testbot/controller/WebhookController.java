package com.ksenia.testbot.controller;


import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.ksenia.testbot.service.EventHandler;
import com.ksenia.testbot.service.MessengerProducer;
import com.ksenia.testbot.utils.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import static java.util.Optional.of;


@RestController
public class WebhookController {


    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);
    @Autowired
    private MessengerProducer messenger;
    @Autowired
    private EventHandler eventHandler;


    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.verify_token") String verifyTokenInput,
                                                @RequestParam("hub.challenge") String challenge) {

        LOGGER.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyTokenInput, challenge);

        try {
            messenger.messengerProducer().verifyWebhook(mode, verifyTokenInput);
        } catch (MessengerVerificationException e) {
            LOGGER.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        return ResponseEntity.status(HttpStatus.OK).body(challenge);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void verifyWebhook(@RequestBody String payload, @RequestHeader("X-Hub-Signature") final String signature) {
        try {
            messenger.messengerProducer().onReceiveEvents(payload, of(signature), event -> {
                try {
                    eventHandler.handle(event);
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
            messenger.messengerProducer().updateSettings(ResponseMessage.greetingText());
            messenger.messengerProducer().updateSettings(ResponseMessage.startedButton());
            messenger.messengerProducer().updateSettings(ResponseMessage.persistentMenu());
        } catch (MessengerApiException | MessengerIOException e) {
            LOGGER.warn("Cannot update settings: {}", e.getMessage());
        }


    }
}