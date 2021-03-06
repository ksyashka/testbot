package com.ksenia.testbot.controller;


import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.ksenia.testbot.service.EventHandlerImpl;
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


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Messenger messenger;
    @Autowired
    private EventHandlerImpl eventHandler;


    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.verify_token") String verifyTokenInput,
                                                @RequestParam("hub.challenge") String challenge) {

        logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyTokenInput, challenge);

        try {
            messenger.verifyWebhook(mode, verifyTokenInput);
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        return ResponseEntity.status(HttpStatus.OK).body(challenge);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void getWebhookEvent(@RequestBody String payload, @RequestHeader(value = "X-Hub-Signature", required = false) final String signature) {
        try {
            messenger.onReceiveEvents(payload, of(signature), event -> {
                try {
                    eventHandler.handle(event);
                } catch (MessengerApiException | MessengerIOException e) {
                    logger.warn("Processing of callback payload failed: {}", e.getMessage());
                }
            });
        } catch (Exception e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void setUp(){
        try {
            messenger.updateSettings(ResponseMessage.greetingText());
            messenger.updateSettings(ResponseMessage.startedButton());
            messenger.updateSettings(ResponseMessage.persistentMenu());
        } catch (MessengerApiException | MessengerIOException e) {
            logger.warn("Cannot update settings: {}", e.getMessage());
        }


    }
}