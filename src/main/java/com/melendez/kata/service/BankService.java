package com.melendez.kata.service;

import com.melendez.kata.service.dto.StatementDTO;

import java.util.Set;

/**
 * This service is responsible to call the database operation of the connected user
 */
public interface BankService {

    StatementDTO depositMoney(StatementDTO amount);

    StatementDTO withdraw(StatementDTO statement);

    Set<StatementDTO> fetchStatements();
}
