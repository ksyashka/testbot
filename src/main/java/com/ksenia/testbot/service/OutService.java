package com.ksenia.testbot.service;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.ksenia.testbot.exceptions.GetCurrentCurrencyException;
import com.ksenia.testbot.utils.CurrencyGetter;
import com.ksenia.testbot.utils.ResponseMessage;
import model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutService.class);
    private Messenger messenger;

    public OutService(Messenger messenger) {
        this.messenger = messenger;
    }

    public OutService() {
    }

    public void sendStartMenu(String recepientId) throws MessengerApiException, MessengerIOException {
        String[] str = {"Registration", "Currency", "Contact"};
        messenger.send(ResponseMessage.listButtons(recepientId, str));

    }

    ;

    public void sendCurrencyMenu(String recepientId) throws MessengerApiException, MessengerIOException {
        String[] str = {"EUR", "USD", "ALL"};
        messenger.send(ResponseMessage.listButtons(recepientId, str));

    }

    public void sendContact(String recepientId) throws MessengerApiException, MessengerIOException {
        messenger.send(ResponseMessage.textMessage(recepientId, "Kiev city \n044 222 22 22"));

    }

    public void sendCurrency(String recepientId, String[] type) throws MessengerApiException, MessengerIOException {

        List<Currency> currency = new ArrayList<>();
        try {
            for (String s : type)
                currency.add(CurrencyGetter.getCurrentCurrency(s));
        } catch (GetCurrentCurrencyException e) {
            LOGGER.warn(e.getMessage());
            messenger.send(ResponseMessage.textMessage(recepientId, "Sorry, service not available, try later"));
        }

        StringBuilder message = new StringBuilder();
        for (Currency c : currency)
            message.append(c.getCc()).append(" = ").append(c.getRate()).append("\n");

        messenger.send(ResponseMessage.textMessage(recepientId, message.toString()));
    }

    public void sendText(String recepientId, String text) throws MessengerApiException, MessengerIOException {
        messenger.send(ResponseMessage.textMessage(recepientId, text));

    }


}
