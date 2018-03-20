package com.ksenia.testbot.service;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.ksenia.testbot.constants.Constants;
import com.ksenia.testbot.constants.PayloadType;
import com.ksenia.testbot.exceptions.GetCurrentCurrencyException;
import com.ksenia.testbot.model.Currency;
import com.ksenia.testbot.utils.CurrencyGetter;
import com.ksenia.testbot.utils.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ksenia.testbot.constants.PayloadType.*;

@Service
public class OutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutService.class);
    @Autowired
    private MessengerProducer messenger;


    public void sendStartMenu(String recipientId) throws MessengerApiException, MessengerIOException {
        String[] str = {"Registration", "Currency", "Contact"};
        PayloadType[] types = {REGISTRATION, CURRENCY, CONTACT};
        messenger.messengerProducer().send(ResponseMessage.listButtons(recipientId, str, types, Constants.TEXT_START_MENU));

    }

    ;

    public void sendCurrencyMenu(String recipientId) throws MessengerApiException, MessengerIOException {
        String[] str = {"EUR", "USD", "ALL"};
        PayloadType[] types = {EUR, USD, ALL};
        messenger.messengerProducer().send(ResponseMessage.listButtons(recipientId, str, types, Constants.TEXT_CURRENCY_MENU));

    }

    public void sendContact(String recipientId) throws MessengerApiException, MessengerIOException {
        messenger.messengerProducer().send(ResponseMessage.textMessage(recipientId, Constants.CONTACT_INFO));

    }

    public void sendCurrency(String recipientId, String[] type) throws MessengerApiException, MessengerIOException {

        List<Currency> currency = new ArrayList<>();
        try {
            for (String s : type) {
                Currency currentCurrency = CurrencyGetter.getCurrentCurrency(s);
                currency.add(currentCurrency);
            }
        } catch (GetCurrentCurrencyException e) {
            LOGGER.warn(e.getMessage());
            messenger.messengerProducer().send(ResponseMessage.textMessage(recipientId, Constants.NOT_AVAILABLE));
        }

        StringBuilder message = new StringBuilder();
        for (Currency c : currency)
            message.append(c.getCurrencyCode()).append(" = ").append(c.getRate()).append("\n");

        messenger.messengerProducer().send(ResponseMessage.textMessage(recipientId, message.toString()));
    }

    public void sendText(String recipientId, String text) throws MessengerApiException, MessengerIOException {
        messenger.messengerProducer().send(ResponseMessage.textMessage(recipientId, text));

    }
}
