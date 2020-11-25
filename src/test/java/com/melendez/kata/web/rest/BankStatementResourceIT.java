package com.melendez.kata.web.rest;

import com.melendez.kata.KataApp;
import com.melendez.kata.domain.BankStatement;
import com.melendez.kata.domain.BankAccount;
import com.melendez.kata.repository.BankStatementRepository;
import com.melendez.kata.service.BankStatementService;
import com.melendez.kata.service.dto.BankStatementDTO;
import com.melendez.kata.service.mapper.BankStatementMapper;
import com.melendez.kata.service.dto.BankStatementCriteria;
import com.melendez.kata.service.BankStatementQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.melendez.kata.domain.enumeration.StatementType;
/**
 * Integration tests for the {@link BankStatementResource} REST controller.
 */
@SpringBootTest(classes = KataApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BankStatementResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_AMOUNT = 1D - 1D;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_VALIDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StatementType DEFAULT_STATEMENT_TYPE = StatementType.DEPOSIT;
    private static final StatementType UPDATED_STATEMENT_TYPE = StatementType.WITHDRAW;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private BankStatementMapper bankStatementMapper;

    @Autowired
    private BankStatementService bankStatementService;

    @Autowired
    private BankStatementQueryService bankStatementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankStatementMockMvc;

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
            .statementType(DEFAULT_STATEMENT_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createEntity(em);
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        bankStatement.setAccount(bankAccount);
        return bankStatement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankStatement createUpdatedEntity(EntityManager em) {
        BankStatement bankStatement = new BankStatement()
            .amount(UPDATED_AMOUNT)
            .label(UPDATED_LABEL)
            .validatedDate(UPDATED_VALIDATED_DATE)
            .statementType(UPDATED_STATEMENT_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createUpdatedEntity(em);
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        bankStatement.setAccount(bankAccount);
        return bankStatement;
    }

    @BeforeEach
    public void initTest() {
        bankStatement = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllBankStatements() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList
        restBankStatementMockMvc.perform(get("/api/bank-statements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankStatement.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].validatedDate").value(hasItem(DEFAULT_VALIDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].statementType").value(hasItem(DEFAULT_STATEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getBankStatement() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get the bankStatement
        restBankStatementMockMvc.perform(get("/api/bank-statements/{id}", bankStatement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankStatement.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.validatedDate").value(DEFAULT_VALIDATED_DATE.toString()))
            .andExpect(jsonPath("$.statementType").value(DEFAULT_STATEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }


    @Test
    @Transactional
    public void getBankStatementsByIdFiltering() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        Long id = bankStatement.getId();

        defaultBankStatementShouldBeFound("id.equals=" + id);
        defaultBankStatementShouldNotBeFound("id.notEquals=" + id);

        defaultBankStatementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankStatementShouldNotBeFound("id.greaterThan=" + id);

        defaultBankStatementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankStatementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount equals to DEFAULT_AMOUNT
        defaultBankStatementShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the bankStatementList where amount equals to UPDATED_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount not equals to DEFAULT_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the bankStatementList where amount not equals to UPDATED_AMOUNT
        defaultBankStatementShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultBankStatementShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the bankStatementList where amount equals to UPDATED_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount is not null
        defaultBankStatementShouldBeFound("amount.specified=true");

        // Get all the bankStatementList where amount is null
        defaultBankStatementShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultBankStatementShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the bankStatementList where amount is greater than or equal to UPDATED_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount is less than or equal to DEFAULT_AMOUNT
        defaultBankStatementShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the bankStatementList where amount is less than or equal to SMALLER_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount is less than DEFAULT_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the bankStatementList where amount is less than UPDATED_AMOUNT
        defaultBankStatementShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where amount is greater than DEFAULT_AMOUNT
        defaultBankStatementShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the bankStatementList where amount is greater than SMALLER_AMOUNT
        defaultBankStatementShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllBankStatementsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where label equals to DEFAULT_LABEL
        defaultBankStatementShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the bankStatementList where label equals to UPDATED_LABEL
        defaultBankStatementShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where label not equals to DEFAULT_LABEL
        defaultBankStatementShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the bankStatementList where label not equals to UPDATED_LABEL
        defaultBankStatementShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultBankStatementShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the bankStatementList where label equals to UPDATED_LABEL
        defaultBankStatementShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where label is not null
        defaultBankStatementShouldBeFound("label.specified=true");

        // Get all the bankStatementList where label is null
        defaultBankStatementShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllBankStatementsByLabelContainsSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where label contains DEFAULT_LABEL
        defaultBankStatementShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the bankStatementList where label contains UPDATED_LABEL
        defaultBankStatementShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where label does not contain DEFAULT_LABEL
        defaultBankStatementShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the bankStatementList where label does not contain UPDATED_LABEL
        defaultBankStatementShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllBankStatementsByValidatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where validatedDate equals to DEFAULT_VALIDATED_DATE
        defaultBankStatementShouldBeFound("validatedDate.equals=" + DEFAULT_VALIDATED_DATE);

        // Get all the bankStatementList where validatedDate equals to UPDATED_VALIDATED_DATE
        defaultBankStatementShouldNotBeFound("validatedDate.equals=" + UPDATED_VALIDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByValidatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where validatedDate not equals to DEFAULT_VALIDATED_DATE
        defaultBankStatementShouldNotBeFound("validatedDate.notEquals=" + DEFAULT_VALIDATED_DATE);

        // Get all the bankStatementList where validatedDate not equals to UPDATED_VALIDATED_DATE
        defaultBankStatementShouldBeFound("validatedDate.notEquals=" + UPDATED_VALIDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByValidatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where validatedDate in DEFAULT_VALIDATED_DATE or UPDATED_VALIDATED_DATE
        defaultBankStatementShouldBeFound("validatedDate.in=" + DEFAULT_VALIDATED_DATE + "," + UPDATED_VALIDATED_DATE);

        // Get all the bankStatementList where validatedDate equals to UPDATED_VALIDATED_DATE
        defaultBankStatementShouldNotBeFound("validatedDate.in=" + UPDATED_VALIDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByValidatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where validatedDate is not null
        defaultBankStatementShouldBeFound("validatedDate.specified=true");

        // Get all the bankStatementList where validatedDate is null
        defaultBankStatementShouldNotBeFound("validatedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankStatementsByStatementTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementType equals to DEFAULT_STATEMENT_TYPE
        defaultBankStatementShouldBeFound("statementType.equals=" + DEFAULT_STATEMENT_TYPE);

        // Get all the bankStatementList where statementType equals to UPDATED_STATEMENT_TYPE
        defaultBankStatementShouldNotBeFound("statementType.equals=" + UPDATED_STATEMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByStatementTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementType not equals to DEFAULT_STATEMENT_TYPE
        defaultBankStatementShouldNotBeFound("statementType.notEquals=" + DEFAULT_STATEMENT_TYPE);

        // Get all the bankStatementList where statementType not equals to UPDATED_STATEMENT_TYPE
        defaultBankStatementShouldBeFound("statementType.notEquals=" + UPDATED_STATEMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByStatementTypeIsInShouldWork() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementType in DEFAULT_STATEMENT_TYPE or UPDATED_STATEMENT_TYPE
        defaultBankStatementShouldBeFound("statementType.in=" + DEFAULT_STATEMENT_TYPE + "," + UPDATED_STATEMENT_TYPE);

        // Get all the bankStatementList where statementType equals to UPDATED_STATEMENT_TYPE
        defaultBankStatementShouldNotBeFound("statementType.in=" + UPDATED_STATEMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByStatementTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementType is not null
        defaultBankStatementShouldBeFound("statementType.specified=true");

        // Get all the bankStatementList where statementType is null
        defaultBankStatementShouldNotBeFound("statementType.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdBy equals to DEFAULT_CREATED_BY
        defaultBankStatementShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the bankStatementList where createdBy equals to UPDATED_CREATED_BY
        defaultBankStatementShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdBy not equals to DEFAULT_CREATED_BY
        defaultBankStatementShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the bankStatementList where createdBy not equals to UPDATED_CREATED_BY
        defaultBankStatementShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultBankStatementShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the bankStatementList where createdBy equals to UPDATED_CREATED_BY
        defaultBankStatementShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdBy is not null
        defaultBankStatementShouldBeFound("createdBy.specified=true");

        // Get all the bankStatementList where createdBy is null
        defaultBankStatementShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllBankStatementsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdBy contains DEFAULT_CREATED_BY
        defaultBankStatementShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the bankStatementList where createdBy contains UPDATED_CREATED_BY
        defaultBankStatementShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdBy does not contain DEFAULT_CREATED_BY
        defaultBankStatementShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the bankStatementList where createdBy does not contain UPDATED_CREATED_BY
        defaultBankStatementShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllBankStatementsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate equals to DEFAULT_CREATED_DATE
        defaultBankStatementShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the bankStatementList where createdDate equals to UPDATED_CREATED_DATE
        defaultBankStatementShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultBankStatementShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the bankStatementList where createdDate not equals to UPDATED_CREATED_DATE
        defaultBankStatementShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultBankStatementShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the bankStatementList where createdDate equals to UPDATED_CREATED_DATE
        defaultBankStatementShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBankStatementsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate is not null
        defaultBankStatementShouldBeFound("createdDate.specified=true");

        // Get all the bankStatementList where createdDate is null
        defaultBankStatementShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankStatementsByAccountIsEqualToSomething() throws Exception {
        // Get already existing entity
        BankAccount account = bankStatement.getAccount();
        bankStatementRepository.saveAndFlush(bankStatement);
        Long accountId = account.getId();

        // Get all the bankStatementList where account equals to accountId
        defaultBankStatementShouldBeFound("accountId.equals=" + accountId);

        // Get all the bankStatementList where account equals to accountId + 1
        defaultBankStatementShouldNotBeFound("accountId.equals=" + (accountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankStatementShouldBeFound(String filter) throws Exception {
        restBankStatementMockMvc.perform(get("/api/bank-statements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankStatement.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].validatedDate").value(hasItem(DEFAULT_VALIDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].statementType").value(hasItem(DEFAULT_STATEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restBankStatementMockMvc.perform(get("/api/bank-statements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankStatementShouldNotBeFound(String filter) throws Exception {
        restBankStatementMockMvc.perform(get("/api/bank-statements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankStatementMockMvc.perform(get("/api/bank-statements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBankStatement() throws Exception {
        // Get the bankStatement
        restBankStatementMockMvc.perform(get("/api/bank-statements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
