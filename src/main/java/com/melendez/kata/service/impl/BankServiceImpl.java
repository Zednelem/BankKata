package com.melendez.kata.service.impl;

import com.melendez.kata.domain.BankAccount;
import com.melendez.kata.domain.BankStatement;
import com.melendez.kata.domain.User;
import com.melendez.kata.provider.SystemTimeProvider;
import com.melendez.kata.repository.BankAccountRepository;
import com.melendez.kata.repository.BankStatementRepository;
import com.melendez.kata.repository.UserRepository;
import com.melendez.kata.security.SecurityUtils;
import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.BankStatementDTO;
import com.melendez.kata.service.mapper.BankStatementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.melendez.kata.domain.enumeration.StatementType.DEPOSIT;
import static com.melendez.kata.domain.enumeration.StatementType.WITHDRAW;

/**
 * Fake implementation
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final List<BankStatementDTO> statements = new ArrayList<>();
    private final BankAccountRepository accountRepo;
    private final BankStatementRepository statementsRepo;
    private final UserRepository userRepo;
    private final SystemTimeProvider time;
    private final BankStatementMapper bankStatementMapper;

    private static class BankResourceException extends RuntimeException {
        private BankResourceException(String message) {
            super(message);
        }
    }

    BankServiceImpl(BankAccountRepository accountRepo,
                    BankStatementRepository statementRepo,
                    UserRepository userRepo,
                    SystemTimeProvider timeProvider,
                    BankStatementMapper bankStatementMapper){
       this.accountRepo = accountRepo;
       this.statementsRepo = statementRepo;
       this.userRepo=userRepo;
       this.time = timeProvider;
       this.bankStatementMapper = bankStatementMapper;
    }

    @Override
    public BankStatementDTO depositMoney(BankStatementDTO statement) {

        statement.setStatementType(DEPOSIT);
        log.warn("Not Implemented in database. Amount deposed: {}", statement);
        return saveStatement(statement);
    }

    private BankStatementDTO saveStatement(BankStatementDTO statement) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new BankResourceException("Current user login not found"));
        User user = userRepo.findOneByLogin(userLogin).orElseThrow(() -> new BankResourceException("User login does not exist"));
        BankAccount account = accountRepo.findFirstByUser(user).orElseThrow(() -> new BankResourceException("Account does not exist"));

        statement.setId(null);
        statement.setAccountId(account.getId());
        statement.setAccountName(account.getName());
        statement.setCreatedBy(userLogin);
        statement.setCreatedDate(time.getDate());
        statement.setValidatedDate(null);
        statement = bankStatementMapper.toDto(statementsRepo.save(bankStatementMapper.toEntity(statement)));
        return statement;
    }

    @Override
    public BankStatementDTO withdraw(BankStatementDTO statement) {
        statement.setStatementType(WITHDRAW);
        log.warn("Not Implemented in database. Amount withdrawn: {}", statement);
        return saveStatement(statement);
    }

    @Override
    public Set<BankStatementDTO> fetchStatements() {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new BankResourceException("Current user login not found"));
        User user = userRepo.findOneByLogin(userLogin).orElseThrow(() -> new BankResourceException("User login does not exist"));
        BankAccount account = accountRepo.findFirstByUser(user).orElseThrow(() -> new BankResourceException("Account does not exist"));
        List<BankStatement> statements = statementsRepo.findBankStatementsByAccount(account);
        return new HashSet<>(this.bankStatementMapper.toDto(statements));
    }



}
