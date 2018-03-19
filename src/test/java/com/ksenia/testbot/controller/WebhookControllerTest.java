package com.ksenia.testbot.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${messenger4j.verifyToken}")
    private String verifyToken;


    @Test
    public void testGetPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/webhook?hub.verify_token=youshallnotmergewithoutcodereview" +
                "&hub.challenge=youshallnotmergewithoutcodereview&hub.mode=subscribe"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(verifyToken));
    }

    @Test
    public void testGetNegative() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/webhook?hub.verify_token=somethingelse" +
                "&hub.challenge=youshallnotmergewithoutcodereview&hub.mode=subscribe"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testPostPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/webhook")
                .content("{\"object\":\"page\",\"entry\":[]}")
                .header("X-Hub-Signature","ssh1="))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
