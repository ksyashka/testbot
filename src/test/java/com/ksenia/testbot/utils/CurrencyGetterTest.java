package com.ksenia.testbot.utils;

import com.ksenia.testbot.exceptions.GetCurrentCurrencyException;
import com.ksenia.testbot.model.Currency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyGetterTest {


    @Test(expected = GetCurrentCurrencyException.class)
    public void getCurrentCurrencyTestNegative() throws GetCurrentCurrencyException {
        String currency = "UAH";
        CurrencyGetter.getCurrentCurrency(currency);
    }

    @Test
    public void getCurrentCurrencyTestPositive() throws GetCurrentCurrencyException {
        String currency = "USD";
        Currency actualCurrency = CurrencyGetter.getCurrentCurrency(currency);
        Currency expectedCurrency = new Currency(840);
        Assert.assertEquals(expectedCurrency,actualCurrency);
    }
}
