package com.ksenia.testbot.controller;


import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;

import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.ksenia.testbot.handler.EventHandler;
import com.ksenia.testbot.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static java.util.Optional.of;


@RestController
public class WebhookController {


    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    private Messenger messenger;

    @Autowired
    WebhookController(@Value("${messenger4j.pageAccessToken}") final String pageAccessToken,
                      @Value("${messenger4j.appSecret}") final String appSecret,
                      @Value("${messenger4j.verifyToken}") final String verifyToken) {
        messenger = Messenger.create(pageAccessToken, appSecret, verifyToken);
    }


    @RequestMapping(value = "/webhook", method = RequestMethod.GET)
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


    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity<String> verifyWebhook(@RequestBody String payload, @RequestHeader("X-Hub-Signature") final String signature) {

        System.out.println(payload);
        System.out.println(signature);

        try {
            messenger.onReceiveEvents(payload, of(signature), event -> {
                EventHandler.handle(event, messenger);
            });
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


//        try {
//            messenger.onReceiveEvents(payload, of(signature), event -> {
//
//
//                final String senderId = event.senderId();
//                final Instant timestamp = event.timestamp();
//
//                if (event.isTextMessageEvent()) {
//                    final TextMessageEvent textMessageEvent = event.asTextMessageEvent();
//                    final String messageId = textMessageEvent.messageId();
//                    final String text = textMessageEvent.text();
//
//                    logger.debug("Received text message from '{}' at '{}' with content: {} (mid: {})",
//                            senderId, timestamp, text, messageId);
//
//                    if ("hello".equals(text)) {
//                        try {
//                            messenger.send(ResponseMessage.textMessage(senderId,"yo"));
//                        } catch (MessengerApiException | MessengerIOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if ("menu".equals(text)) {
//                        try {
//                            messenger.send(ResponseMessage.listButtons(senderId));
//                        } catch (MessengerApiException | MessengerIOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//
//            });
//        } catch (MessengerVerificationException e) {
//            logger.warn("Processing of callback payload failed: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}