package com.ksenia.testbot.controller;


import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class WebhookController {


    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);

    private Messenger messenger;

    @Autowired
    WebhookController(@Value ("${messenger4j.pageAccessToken}") final String pageAccessToken,
                      @Value("${messenger4j.appSecret}") final String appSecret,
                      @Value("${messenger4j.verifyToken}") final String verifyToken){
        messenger = Messenger.create(pageAccessToken, appSecret, verifyToken);
    }




    @RequestMapping(value = "/webhook", method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                @RequestParam("hub.verify_token") String verifyTokenInput,
                                @RequestParam("hub.challenge") String challenge) {

        LOGGER.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyTokenInput, challenge);

        try {
            messenger.verifyWebhook(mode,verifyTokenInput);
        } catch (MessengerVerificationException e) {
            LOGGER.warn("Webhook verification failed: {}", e.getMessage());
            return new ResponseEntity<> (HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>(challenge,HttpStatus.OK);
    }


}
