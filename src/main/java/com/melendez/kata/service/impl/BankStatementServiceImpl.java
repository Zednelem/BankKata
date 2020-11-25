package com.melendez.kata.service.impl;

import com.melendez.kata.service.BankStatementService;
import com.melendez.kata.domain.BankStatement;
import com.melendez.kata.repository.BankStatementRepository;
import com.melendez.kata.service.dto.BankStatementDTO;
import com.melendez.kata.service.mapper.BankStatementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BankStatement}.
 */
@Service
@Transactional
public class BankStatementServiceImpl implements BankStatementService {

    private final Logger log = LoggerFactory.getLogger(BankStatementServiceImpl.class);

    private final BankStatementRepository bankStatementRepository;

    private final BankStatementMapper bankStatementMapper;

    public BankStatementServiceImpl(BankStatementRepository bankStatementRepository, BankStatementMapper bankStatementMapper) {
        this.bankStatementRepository = bankStatementRepository;
        this.bankStatementMapper = bankStatementMapper;
    }

    @Override
    public BankStatementDTO save(BankStatementDTO bankStatementDTO) {
        log.debug("Request to save BankStatement : {}", bankStatementDTO);
        BankStatement bankStatement = bankStatementMapper.toEntity(bankStatementDTO);
        bankStatement = bankStatementRepository.save(bankStatement);
        return bankStatementMapper.toDto(bankStatement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankStatementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankStatements");
        return bankStatementRepository.findAll(pageable)
            .map(bankStatementMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BankStatementDTO> findOne(Long id) {
        log.debug("Request to get BankStatement : {}", id);
        return bankStatementRepository.findById(id)
            .map(bankStatementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankStatement : {}", id);
        bankStatementRepository.deleteById(id);
    }
}
