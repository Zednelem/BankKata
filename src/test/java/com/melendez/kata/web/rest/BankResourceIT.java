package com.melendez.kata.web.rest;

import com.melendez.kata.KataApp;
import com.melendez.kata.domain.BankAccount;
import com.melendez.kata.domain.BankStatement;
import com.melendez.kata.domain.User;
import com.melendez.kata.domain.enumeration.StatementType;
import com.melendez.kata.provider.MockedSystemTime;
import com.melendez.kata.security.AuthoritiesConstants;
import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.BankStatementDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * To be reworked as a full Integration Test
 *
 * @see BankResource
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.USER, value = UserResourceIT.DEFAULT_LOGIN)
@SpringBootTest(classes = KataApp.class)
class BankResourceIT {


    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    public static final Double DEFAULT_AMOUNT = 1000.0;
    public static final String DEFAULT_CREATED_BY = "DEFAULT CREATED BY FIELD";
    public static final Instant DEFAULT_CREATED_DATE = MockedSystemTime.DEFAULT_NOW_DATE;
    public static final Instant DEFAULT_VALIDATED_DATE = MockedSystemTime.DEFAULT_NOW_DATE;
    public static final String DEFAULT_LABEL = "DEFAULT LABEL";
    private static final StatementType DEFAULT_STATEMENT_TYPE = StatementType.DEPOSIT;

    @Autowired
    private BankService bankService;
    @Autowired
    private MockMvc restMockMvc;

    @Autowired
    private EntityManager em;

    private BankStatementDTO inputStatement;
    private BankStatement bankStatement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankStatement createEntity(EntityManager em) {
        BankStatement bankStatement = new BankStatement()
            .amount(DEFAULT_AMOUNT)
            .label(DEFAULT_LABEL)
            .validatedDate(DEFAULT_VALIDATED_DATE)
            .statementType(DEFAULT_STATEMENT_TYPE);
        // Add required entity
        BankAccount bankAccount;


        bankAccount = BankAccountResourceIT.createEntityWithDefaultUser(em);
        em.persist(bankAccount);
        em.flush();

        bankStatement.setAccount(bankAccount);
        return bankStatement;
    }

    @BeforeEach
    public void initTest() {
        bankStatement = createEntity(em);
        em.persist(bankStatement);
        em.flush();
        inputStatement = initInputs();
    }

    private BankStatementDTO initInputs() {
        BankStatementDTO statement = new BankStatementDTO();
        statement.setId(null);
        statement.setAmount(DEFAULT_AMOUNT);
        statement.setCreatedBy(DEFAULT_CREATED_BY);
        statement.setCreatedDate(DEFAULT_CREATED_DATE);
        statement.setStatementType(null);
        statement.setLabel(DEFAULT_LABEL);
        statement.setValidatedDate(null);
        return statement;
    }

    /**
     * Test depositMoney
     */
    @Test
    @Transactional
    void testDepositMoney() throws Exception {
        MockHttpServletResponse response = restMockMvc.perform(
            post("/api/bank/actions/deposit-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated())
            // TODO: 24/11/2020 : attach the createdBy and Created Date
            //  .andExpect(jsonPath("$.createdBy").value("DEFAULT CREATED BY FIELD"))
            .andExpect(jsonPath("$.amount").value(1000.0))
            .andExpect(jsonPath("$.statementType").value("DEPOSIT")).andReturn().getResponse();

    }

    @Test
    @Transactional
    void testWithdrawMoney() throws Exception {
        MockHttpServletResponse response =restMockMvc.perform(
            post("/api/bank/actions/withdraw-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated())     .andExpect(status().isCreated()).andExpect(status().isCreated())
            // TODO: 24/11/2020 : attach the createdBy and Created Date
            //  .andExpect(jsonPath("$.createdBy").value("DEFAULT CREATED BY FIELD"))
            .andExpect(jsonPath("$.amount").value(1000.0))
            .andExpect(jsonPath("$.statementType").value("WITHDRAW")).andReturn().getResponse();
    }

    @Test
    @Transactional
    void testGetStatements() throws Exception {
        restMockMvc.perform(
            post("/api/bank/actions/deposit-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated());
        restMockMvc.perform(
            post("/api/bank/actions/withdraw-money")
                .contentType(APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(inputStatement)))
            .andExpect(status().isCreated());

        MockHttpServletResponse response =restMockMvc.perform(get("/api/bank/statements"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].statementType").value(hasItem("WITHDRAW")))
            .andExpect(jsonPath("$.[*].statementType").value(hasItem("DEPOSIT")))
            // TODO: 24/11/2020 : maked a test on createdBy and createdDate Field
            //  .andExpect(jsonPath("$.[*].createdBy").value("DEFAULT CREATED BY FIELD"))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(1000.0)))
            .andReturn().getResponse();

    }
}
