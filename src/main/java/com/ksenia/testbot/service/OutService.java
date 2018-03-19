package com.ksenia.testbot.service;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.persistentmenu.LocalizedPersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.action.PostbackCallToAction;
import com.ksenia.testbot.enums.PayloadType;
import com.ksenia.testbot.exceptions.GetCurrentCurrencyException;
import com.ksenia.testbot.utils.CurrencyGetter;
import com.ksenia.testbot.utils.ResponseMessage;
import com.ksenia.testbot.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class OutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutService.class);
    private Messenger messenger;

    public OutService(Messenger messenger) {
        this.messenger = messenger;
    }

    public OutService() {
    }

    public void sendStartMenu(String recipientId) throws MessengerApiException, MessengerIOException {
        String[] str = {"Registration", "Currency", "Contact"};
        messenger.send(ResponseMessage.listButtons(recipientId, str, "Make your choice :)"));

    }

    ;

    public void sendCurrencyMenu(String recipientId) throws MessengerApiException, MessengerIOException {
        String[] str = {"EUR", "USD", "ALL"};
        messenger.send(ResponseMessage.listButtons(recipientId, str, "Choose the currency :)"));

    }

    public void sendContact(String recipientId) throws MessengerApiException, MessengerIOException {
        messenger.send(ResponseMessage.textMessage(recipientId, "Kiev city \n044 222 22 22"));

    }

    public void sendCurrency(String recipientId, String[] type) throws MessengerApiException, MessengerIOException {

        List<Currency> currency = new ArrayList<>();
        try {
            for (String s : type)
                currency.add(CurrencyGetter.getCurrentCurrency(s));
        } catch (GetCurrentCurrencyException e) {
            LOGGER.warn(e.getMessage());
            messenger.send(ResponseMessage.textMessage(recipientId, "Sorry, service not available, try later"));
        }

        StringBuilder message = new StringBuilder();
        for (Currency c : currency)
            message.append(c.getCc()).append(" = ").append(c.getRate()).append("\n");

        messenger.send(ResponseMessage.textMessage(recipientId, message.toString()));
    }

    public void sendText(String recepientId, String text) throws MessengerApiException, MessengerIOException {
        messenger.send(ResponseMessage.textMessage(recepientId, text));

    }
}
