package com.ksenia.testbot.utils;

import model.Currency;
import com.ksenia.testbot.exceptions.GetCurrentCurrencyException;
import org.codehaus.jackson.map.ObjectMapper;
import java.net.URL;


public class CurrencyGetter {

    public static Currency getCurrentCurrency(String type) throws GetCurrentCurrencyException {
        try {
            URL url = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=" + type + "&json");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(url, Currency[].class)[0];
        } catch (Exception e) {
            throw new GetCurrentCurrencyException(e.getMessage());
        }
    }

}