package com.melendez.kata.web.rest;

import com.melendez.kata.KataApp;
import com.melendez.kata.provider.MockedSystemTime;
import com.melendez.kata.security.AuthoritiesConstants;
import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.StatementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * To be reworked as a full Integration Test
 *
 * @see BankResource
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.USER)
@SpringBootTest(classes = KataApp.class)
class BankResourceIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    public static final Long DEFAULT_AMOUNT = 1000L;
    public static final String DEFAULT_CREATED_BY = "DEFAULT CREATED BY FIELD";
    public static final Instant DEFAULT_CREATED_DATE = MockedSystemTime.DEFAULT_NOW_DATE;
    public static final Instant SECURITY_VALIDATED_DATE = MockedSystemTime.DEFAULT_NOW_DATE;
    public static final String DEFAULT_LABEL = "DEFAULT LABEL";

    @Autowired
    private BankService bankService;
    @Autowired
    private MockMvc restMockMvc;

    private StatementDTO inputStatement;

    @BeforeEach
    public void setUp() {
        inputStatement = initInputs();
    }

    private StatementDTO initInputs() {
        StatementDTO statement = new StatementDTO();
        statement.setId(null);
        statement.setAmount(DEFAULT_AMOUNT);
        statement.setCreatedBy(DEFAULT_CREATED_BY);
        statement.setCreatedDate(DEFAULT_CREATED_DATE);
        statement.setType(null);
        statement.setLabel(DEFAULT_LABEL);
        statement.setValidatedDate(null);
        return statement;
    }

    /**
     * Test depositMoney
     */
    @Test
    void testDepositMoney() throws Exception {
        MockHttpServletResponse response = restMockMvc.perform(
            post("/api/bank/actions/deposit-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated()).andReturn().getResponse();
        assertTrue(response.getContentAsString().contains("\"createdBy\":\"DEFAULT CREATED BY FIELD\""));
        assertTrue(response.getContentAsString().contains("\"amount\":1000.0"));

    }

    @Test
    void testWithdrawMoney() throws Exception {
        MockHttpServletResponse response =restMockMvc.perform(
            post("/api/bank/actions/withdraw-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated())     .andExpect(status().isCreated()).andReturn().getResponse();
        assertTrue(response.getContentAsString().contains("\"createdBy\":\"DEFAULT CREATED BY FIELD\""));
        assertTrue(response.getContentAsString().contains("\"amount\":1000.0"));
    }

    @Test
    void testGetStatements() throws Exception {
        restMockMvc.perform(
            post("/api/bank/actions/deposit-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated());
        restMockMvc.perform(
            post("/api/bank/actions/withdraw-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated());

        MockHttpServletResponse response =restMockMvc.perform(get("/api/bank/fetch-statements"))
            .andExpect(status().isOk()).andReturn().getResponse();
       // assertTrue(response.getContentAsString().contains("\"createdBy\":\"DEFAULT CREATED BY FIELD\""));
       // assertTrue(response.getContentAsString().contains("\"amount\":1000.0"));
        assertTrue(response.getContentAsString().contains("\"WITHDRAW\""));
        assertTrue(response.getContentAsString().contains("\"DEPOSIT\""));

    }
}
