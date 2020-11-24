package com.melendez.kata.web.rest;

import com.melendez.kata.KataApp;
import com.melendez.kata.domain.BankAccount;
import com.melendez.kata.domain.User;
import com.melendez.kata.repository.BankAccountRepository;
import com.melendez.kata.service.BankAccountService;
import com.melendez.kata.service.dto.BankAccountDTO;
import com.melendez.kata.service.mapper.BankAccountMapper;
import com.melendez.kata.service.dto.BankAccountCriteria;
import com.melendez.kata.service.BankAccountQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BankAccountResource} REST controller.
 */
@SpringBootTest(classes = KataApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BankAccountResourceIT {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountQueryService bankAccountQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .balance(DEFAULT_BALANCE)
            .name(DEFAULT_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        bankAccount.setUser(user);
        return bankAccount;
    }
    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntityWithDefaultUser(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .balance(DEFAULT_BALANCE)
            .name(DEFAULT_NAME);
        // Add required entity
        User user = UserResourceIT.createEntityWithDefaultLogin(em);
        em.persist(user);
        em.flush();
        bankAccount.setUser(user);
        return bankAccount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createUpdatedEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
            .balance(UPDATED_BALANCE)
            .name(UPDATED_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        bankAccount.setUser(user);
        return bankAccount;
    }

    @BeforeEach
    public void initTest() {
        bankAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getBankAccountsByIdFiltering() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        Long id = bankAccount.getId();

        defaultBankAccountShouldBeFound("id.equals=" + id);
        defaultBankAccountShouldNotBeFound("id.notEquals=" + id);

        defaultBankAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultBankAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance equals to DEFAULT_BALANCE
        defaultBankAccountShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the bankAccountList where balance equals to UPDATED_BALANCE
        defaultBankAccountShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance not equals to DEFAULT_BALANCE
        defaultBankAccountShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the bankAccountList where balance not equals to UPDATED_BALANCE
        defaultBankAccountShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultBankAccountShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the bankAccountList where balance equals to UPDATED_BALANCE
        defaultBankAccountShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance is not null
        defaultBankAccountShouldBeFound("balance.specified=true");

        // Get all the bankAccountList where balance is null
        defaultBankAccountShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance is greater than or equal to DEFAULT_BALANCE
        defaultBankAccountShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the bankAccountList where balance is greater than or equal to UPDATED_BALANCE
        defaultBankAccountShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance is less than or equal to DEFAULT_BALANCE
        defaultBankAccountShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the bankAccountList where balance is less than or equal to SMALLER_BALANCE
        defaultBankAccountShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance is less than DEFAULT_BALANCE
        defaultBankAccountShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the bankAccountList where balance is less than UPDATED_BALANCE
        defaultBankAccountShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where balance is greater than DEFAULT_BALANCE
        defaultBankAccountShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the bankAccountList where balance is greater than SMALLER_BALANCE
        defaultBankAccountShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }


    @Test
    @Transactional
    public void getAllBankAccountsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name equals to DEFAULT_NAME
        defaultBankAccountShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the bankAccountList where name equals to UPDATED_NAME
        defaultBankAccountShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name not equals to DEFAULT_NAME
        defaultBankAccountShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the bankAccountList where name not equals to UPDATED_NAME
        defaultBankAccountShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBankAccountShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the bankAccountList where name equals to UPDATED_NAME
        defaultBankAccountShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name is not null
        defaultBankAccountShouldBeFound("name.specified=true");

        // Get all the bankAccountList where name is null
        defaultBankAccountShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllBankAccountsByNameContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name contains DEFAULT_NAME
        defaultBankAccountShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the bankAccountList where name contains UPDATED_NAME
        defaultBankAccountShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBankAccountsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccountList where name does not contain DEFAULT_NAME
        defaultBankAccountShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the bankAccountList where name does not contain UPDATED_NAME
        defaultBankAccountShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllBankAccountsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = bankAccount.getUser();
        bankAccountRepository.saveAndFlush(bankAccount);
        Long userId = user.getId();

        // Get all the bankAccountList where user equals to userId
        defaultBankAccountShouldBeFound("userId.equals=" + userId);

        // Get all the bankAccountList where user equals to userId + 1
        defaultBankAccountShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankAccountShouldBeFound(String filter) throws Exception {
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restBankAccountMockMvc.perform(get("/api/bank-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankAccountShouldNotBeFound(String filter) throws Exception {
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankAccountMockMvc.perform(get("/api/bank-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
