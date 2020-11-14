package com.melendez.kata.web.rest;

import com.melendez.kata.KataApp;
import com.melendez.kata.provider.MockedSystemTime;
import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.StatementDTO;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * To be reworked as a full Integration Test
 *
 * @see BankResource
 */
@Ignore("To be included when BankRepositoy will be implemented and database configured")
@SpringBootTest(classes = KataApp.class)
class BankResourceIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    public static final Long DEFAULT_AMOUNT = 1000L;
    public static final String DEFAULT_CREATED_BY = "DEFAULT_CREATED_BY_FIELD";
    public static final Instant DEFAULT_CREATED_DATE = MockedSystemTime.DEFAULT_NOW_DATE;
    public static final Instant SECURITY_VALIDATED_DATE = MockedSystemTime.DEFAULT_NOW_DATE;
    public static final String DEFAULT_LABEL = "DEFAULT_LABEL";

    @Mock
    private BankService bankService;

    private MockMvc restMockMvc;
    private StatementDTO inputStatement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inputStatement = initInputs();
        BankResource bankResource = new BankResource(bankService);

        restMockMvc = MockMvcBuilders
            .standaloneSetup(bankResource)
            .build();
    }

    private StatementDTO initInputs() {
        StatementDTO statement = new StatementDTO();
        statement.setId(null);
        statement.setAmount(DEFAULT_AMOUNT);
        statement.setCreatedBy(DEFAULT_CREATED_BY);
        statement.setCreatedDate(DEFAULT_CREATED_DATE);
        statement.setType(null);
        statement.setLabel(DEFAULT_LABEL);
        statement.setValidatedDate(SECURITY_VALIDATED_DATE);
        return statement;
    }

    /**
     * Test depositMoney
     */
    @Test
    void testDepositMoney() throws Exception {

        when(bankService.depositMoney(any())).thenReturn(inputStatement);

        restMockMvc.perform(
            post("/api/bank/deposit-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonString(inputStatement)))
            .andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.content().json(TestUtil.convertObjectToJsonString(inputStatement)));
    }

    @Test
    void testWithdrawMoney() throws Exception {
        when(bankService.withdraw(any())).thenReturn(inputStatement);

        restMockMvc.perform(
            post("/api/bank/withdraw-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonString(inputStatement)))
            .andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.content().string(TestUtil.convertObjectToJsonString(inputStatement)));
    }

    @Test
    void testGetStatements() throws Exception {
        restMockMvc.perform(get("/api/bank/fetch-statements"))
            .andExpect(status().isOk());
    }
}
