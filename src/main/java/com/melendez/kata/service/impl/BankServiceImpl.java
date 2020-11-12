package com.melendez.kata.service.impl;

import com.melendez.kata.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    @Override
    public String depositMoney(double amount) {
        if (amount<=0 ){
            throw new IllegalArgumentException("Amount deposit should be strictly positive: amount queried "+amount);
        }
        log.debug("Not Implemented in database. Amount deposed: {}",amount);
        return "OPERATION_ID";
    }
}
