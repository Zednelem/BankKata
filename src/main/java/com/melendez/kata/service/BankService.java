package com.melendez.kata.service;

import com.melendez.kata.service.dto.BankStatementDTO;

import java.util.Set;

/**
 * This service is responsible to call the database operation of the connected user
 */
public interface BankService {

    BankStatementDTO depositMoney(BankStatementDTO amount);

    BankStatementDTO withdraw(BankStatementDTO statement);

    Set<BankStatementDTO> fetchStatements();
}
