package com.ksenia.testbot.utils;

import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.greeting.Greeting;
import com.github.messenger4j.messengerprofile.greeting.LocalizedGreeting;
import com.github.messenger4j.messengerprofile.persistentmenu.LocalizedPersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.action.PostbackCallToAction;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.template.ButtonTemplate;
import com.github.messenger4j.send.message.template.ListTemplate;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.common.Element;
import com.ksenia.testbot.enums.PayloadType;

import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class ResponseMessage {

    public static MessagePayload textMessage(String recipientId, String text) {
        return MessagePayload.create(recipientId,
                MessagingType.RESPONSE, TextMessage.create(text));
    }

    public static MessagePayload listButtons(String recipientId, String[] names, String text) {
        final List<Button> buttons = new LinkedList<>();
        for (String s : names) {
            buttons.add(PostbackButton.create(s, s));
        }
        final ButtonTemplate buttonTemplate = ButtonTemplate.create(text, buttons);

        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        return MessagePayload.create(recipientId, MessagingType.RESPONSE,
                templateMessage);
    }

    public static MessengerSettings startedButton(){
        return MessengerSettings.create(of(StartButton.create("START")),
                empty(), empty(), empty(), empty(), empty(), empty());
    }

    public static MessengerSettings persistentMenu(){
        final PostbackCallToAction callToActionA = PostbackCallToAction.create("Start from beginning", "START");
        final PostbackCallToAction callToActionB = PostbackCallToAction.create("Contact information", "CONTACT");

        final PersistentMenu persistentMenu = PersistentMenu.create(false, of(Arrays.asList(callToActionA, callToActionB)),
                LocalizedPersistentMenu.create(SupportedLocale.zh_CN, false, empty()));

        return  MessengerSettings.create(empty(), empty(), of(persistentMenu),
                empty(), empty(), empty(), empty());
    }

    public static MessengerSettings greetingtext(){
        final Greeting greeting = Greeting.create("Hello :)", LocalizedGreeting.create(SupportedLocale.en_US,
                ""));
        return MessengerSettings.create(empty(), of(greeting), empty(),
                empty(), empty(), empty(), empty());

    }
}

