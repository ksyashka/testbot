package com.ksenia.testbot.utils;

import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.template.ButtonTemplate;
import com.github.messenger4j.send.message.template.ListTemplate;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.common.Element;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.Optional.of;

public class ResponseMessage {

    public static MessagePayload textMessage(String recipientId, String text) {
        return MessagePayload.create(recipientId,
                MessagingType.RESPONSE, TextMessage.create(text));
    }

    public static MessagePayload listButtons(String recipientId, String[] names) {

        final List<Button> buttons = new LinkedList<>();
        for (String s : names) {
            buttons.add(PostbackButton.create(s, s));
        }
        final ButtonTemplate buttonTemplate = ButtonTemplate.create("Make your choice :)", buttons);

        final TemplateMessage templateMessage = TemplateMessage.create(buttonTemplate);
        return MessagePayload.create(recipientId, MessagingType.RESPONSE,
                templateMessage);

    }
}

