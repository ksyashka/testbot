package com.ksenia.testbot.handler;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.ksenia.testbot.exceptions.GetCurrentCurrencyException;
import com.ksenia.testbot.utils.CurrencyGetter;
import com.ksenia.testbot.utils.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

import static java.util.Optional.of;


public class EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    public static void handle(Event event, Messenger messenger) throws MessengerApiException, MessengerIOException {

        final String senderId = event.senderId();
        final Instant timestamp = event.timestamp();

        if (event.isPostbackEvent()) {
            PostbackEvent postbackEvent = event.asPostbackEvent();
            final Optional<String> payload = postbackEvent.payload();

            logger.debug("Received payload from '{}' at '{}' with payload {}",
                    senderId, timestamp, payload);

            if (of("Start").equals(payload)) {
                String[] str = {"Registration", "Currency", "Contact"};
                messenger.send(ResponseMessage.listButtons(senderId, str));
            }
            if (of("Contact").equals(payload)) {
                messenger.send(ResponseMessage.textMessage(senderId, "Kiev city \n044 222 22 22"));
            }

            if (of("Registration").equals(payload)) System.out.println("yes");

            if (of("Currency").equals(payload)) {
                String[] str = {"EUR", "USD", "ALL"};
                messenger.send(ResponseMessage.listButtons(senderId, str));
            }

            if (of("EUR").equals(payload)) {
                double currency = 0;
                try {
                    currency = CurrencyGetter.getCurrentCurrency("EUR").getRate();
                } catch (GetCurrentCurrencyException e) {
                    logger.warn(e.getMessage());
                    messenger.send(ResponseMessage.textMessage(senderId, "Sorry, service not available, try later"));
                }
                messenger.send(ResponseMessage.textMessage(senderId, String.format("EUR = %f",currency)));
            }
            if (of("USD").equals(payload)) {
                double currency = 0;
                try {
                    currency = CurrencyGetter.getCurrentCurrency("USD").getRate();
                } catch (GetCurrentCurrencyException e) {
                    logger.warn(e.getMessage());
                    messenger.send(ResponseMessage.textMessage(senderId, "Sorry, service not available, try later"));
                }
                messenger.send(ResponseMessage.textMessage(senderId, String.format("USD = %f",currency)));
            }

            if (of("ALL").equals(payload)) {
                double eur = 0;
                double usd = 0;
                try {
                    eur = CurrencyGetter.getCurrentCurrency("EUR").getRate();
                    usd = CurrencyGetter.getCurrentCurrency("USD").getRate();
                } catch (GetCurrentCurrencyException e) {
                    logger.warn(e.getMessage());
                }
                    messenger.send(ResponseMessage.textMessage(senderId, String.format("EUR = %f\nUSD = %f\n",eur,usd)));
            }

        }

        if (event.isTextMessageEvent()) {
            TextMessageEvent textMessageEvent = event.asTextMessageEvent();

            final String messageId = textMessageEvent.messageId();
            final String text = textMessageEvent.text();

            logger.debug("Received text message from '{}' at '{}' with content: {} (mid: {})",
                    senderId, timestamp, text, messageId);
        }
    }
}
