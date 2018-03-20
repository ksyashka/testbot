package com.ksenia.testbot.service;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.webhook.Event;

public interface EventHandler {
    void handle(Event event) throws MessengerApiException, MessengerIOException;
}
