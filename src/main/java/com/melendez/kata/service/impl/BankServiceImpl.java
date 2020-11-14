package com.melendez.kata.service.impl;

import com.melendez.kata.provider.TimeProvider;
import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.StatementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Fake implementation
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final List<StatementDTO> statements = new ArrayList<>();
    private final TimeProvider systemTime;

    BankServiceImpl(TimeProvider systemTime){
        this.systemTime = systemTime;
    }

    @Override
    public StatementDTO depositMoney(StatementDTO statement) {
        statement.setType(StatementDTO.StatementType.DEPOSIT);
        log.warn("Not Implemented in database. Amount deposed: {}",statement);
        return saveStatement(statement);
    }

    private StatementDTO saveStatement(StatementDTO statement) {
        statement.setCreatedDate(systemTime.getDate());
        statement.setValidatedDate(null);
        statement.setId((long) statements.size());
        statements.add(statement);
        return statement;
    }

    @Override
    public StatementDTO withdraw(StatementDTO statement) {
        statement.setType(StatementDTO.StatementType.WITHDRAW);
        log.warn("Not Implemented in database. Amount withdrawn: {}", statement);
        return saveStatement(statement);
    }

    @Override
    public Set<StatementDTO> fetchStatements() {
        return new HashSet<>(this.statements);
    }


}
