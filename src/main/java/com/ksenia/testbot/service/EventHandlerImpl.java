package com.ksenia.testbot.service;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.ksenia.testbot.constants.PayloadType;
import com.ksenia.testbot.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.ksenia.testbot.constants.Constants.*;

@Component
public class EventHandlerImpl implements EventHandler{

    private  final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, UserProfile> userProfiles = new HashMap<>();

    @Autowired
    private OutService outService;

    public void handle(Event event) throws MessengerApiException, MessengerIOException {

        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        if (event.isPostbackEvent()) {
            processPostbackEvent(event, senderId, timestamp);

        }

        else if (event.isTextMessageEvent()) {
            processTextEvent(event, senderId, timestamp);

        }
    }

    private void processTextEvent(Event event, String senderId, Instant timestamp) throws MessengerApiException, MessengerIOException {
        TextMessageEvent textMessageEvent = event.asTextMessageEvent();

        String messageId = textMessageEvent.messageId();
        String text = textMessageEvent.text();

        logger.debug("Received text message from '{}' at '{}' with content: {} (mid: {})",
                senderId, timestamp, text, messageId);


        switch (userProfiles.get(senderId).getRegistrationMarker()) {
            case 1:
                userProfiles.get(senderId).setName(text);
                outService.sendText(senderId, REGISTRATION_SURNAME);
                userProfiles.get(senderId).setRegistrationMarker(2);
                break;
            case 2:
                userProfiles.get(senderId).setSurname(text);
                outService.sendText(senderId, REGISTRATION_AGE);
                userProfiles.get(senderId).setRegistrationMarker(3);
                break;
            case 3:
                userProfiles.get(senderId).setAge(text);
                outService.sendText(senderId, REGISTRATION_SEX);
                userProfiles.get(senderId).setRegistrationMarker(4);
                break;
            case 4:
                userProfiles.get(senderId).setSex(text);
                outService.sendText(senderId, REGISTRATION_LANGUAGE);
                userProfiles.get(senderId).setRegistrationMarker(5);
                break;
            case 5:
                userProfiles.get(senderId).setLanguage(text);
                userProfiles.get(senderId).setRegistrationMarker(0);
                outService.sendText(senderId, userProfiles.get(senderId).toString());
                outService.sendStartMenu(senderId);
                break;
            default:
                outService.sendStartMenu(senderId);
        }
    }

    private void processPostbackEvent(Event event, String senderId, Instant timestamp) throws MessengerApiException, MessengerIOException {
        userProfiles.get(senderId).setRegistrationMarker(0);
        PostbackEvent postbackEvent = event.asPostbackEvent();
        final Optional<String> payload = postbackEvent.payload();

        PayloadType payloadValue = PayloadType.valueOf(payload.get());

        logger.debug("Received payload from '{}' at '{}' with payload {}",
                senderId, timestamp, payload);

        switch (payloadValue) {
            case REGISTRATION:
                if (!userProfiles.containsKey(senderId))
                    userProfiles.put(senderId, new UserProfile(senderId));
                outService.sendText(senderId, REGISTRATION_NAME);
                userProfiles.get(senderId).setRegistrationMarker(1);
                break;
            case START:
                outService.sendStartMenu(senderId);
                break;
            case CONTACT:
                outService.sendContact(senderId);
                outService.sendStartMenu(senderId);
                break;
            case CURRENCY:
                outService.sendCurrencyMenu(senderId);
                break;
            case EUR:
                outService.sendCurrency(senderId, new String[]{"EUR"});
                outService.sendStartMenu(senderId);
                break;
            case USD:
                outService.sendCurrency(senderId, new String[]{"USD"});
                outService.sendStartMenu(senderId);
                break;
            case ALL:
                outService.sendCurrency(senderId, new String[]{"EUR", "USD"});
                outService.sendStartMenu(senderId);
                break;
            default:
                outService.sendStartMenu(senderId);
        }
    }
}
