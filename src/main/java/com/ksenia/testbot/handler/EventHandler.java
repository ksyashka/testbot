package com.ksenia.testbot.handler;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.ksenia.testbot.service.OutService;
import model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

import static java.util.Optional.of;

@Component
public class EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);
 //   private int registrationMarker;
    private HashMap<String, UserProfile> userProfiles;

    public EventHandler() {
    }

    public EventHandler(HashMap<String, UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public void handle(Event event, Messenger messenger) throws MessengerApiException, MessengerIOException {

        OutService outService = new OutService(messenger);

        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        if(!userProfiles.containsKey(senderId))
         userProfiles.put(senderId,new UserProfile(senderId));

        if (event.isPostbackEvent()) {
            PostbackEvent postbackEvent = event.asPostbackEvent();
            final Optional<String> payload = postbackEvent.payload();

            LOGGER.debug("Received payload from '{}' at '{}' with payload {}",
                    senderId, timestamp, payload);


            if (of("Registration").equals(payload)) {
                outService.sendText(senderId, "Please answer the following questions");
                outService.sendText(senderId, "your name?");
                userProfiles.get(senderId).setRegistrationMarker(1);
            }

            if (of("Start").equals(payload)) {
                outService.sendStartMenu(senderId);
            }
            if (of("Contact").equals(payload)) {
                outService.sendContact(senderId);
                outService.sendStartMenu(senderId);
            }

            if (of("Currency").equals(payload)) {
                outService.sendCurrencyMenu(senderId);
            }

            if (of("EUR").equals(payload)) {
                String[] currency = {"EUR"};
                outService.sendCurrency(senderId, currency);
                outService.sendStartMenu(senderId);

            }
            if (of("USD").equals(payload)) {
                String[] currency = {"USD"};
                outService.sendCurrency(senderId, currency);
                outService.sendStartMenu(senderId);
            }

            if (of("ALL").equals(payload)) {
                String[] currency = {"EUR", "USD"};
                outService.sendCurrency(senderId, currency);
                outService.sendStartMenu(senderId);
            }
        }

        if (event.isTextMessageEvent()) {
            TextMessageEvent textMessageEvent = event.asTextMessageEvent();

            final String messageId = textMessageEvent.messageId();
            final String text = textMessageEvent.text();

            LOGGER.debug("Received text message from '{}' at '{}' with content: {} (mid: {})",
                    senderId, timestamp, text, messageId);


            switch (userProfiles.get(senderId).getRegistrationMarker()) {
                case 1:
                    userProfiles.get(senderId).setName(text);
                    outService.sendText(senderId, "your surname?");
                    userProfiles.get(senderId).setRegistrationMarker(2);
                    break;
                case 2:
                    userProfiles.get(senderId).setSurname(text);
                    outService.sendText(senderId, "your age?");
                    userProfiles.get(senderId).setRegistrationMarker(3);
                    break;
                case 3:
                    userProfiles.get(senderId).setAge(text);
                    outService.sendText(senderId, "your sex?");
                    userProfiles.get(senderId).setRegistrationMarker(4);
                    break;
                case 4:
                    userProfiles.get(senderId).setSex(text);
                    outService.sendText(senderId, "your language?");
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
    }
}
