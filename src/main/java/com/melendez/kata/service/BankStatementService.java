package com.melendez.kata.service;

import com.melendez.kata.service.dto.BankStatementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.melendez.kata.domain.BankStatement}.
 */
public interface BankStatementService {

    /**
     * Save a bankStatement.
     *
     * @param bankStatementDTO the entity to save.
     * @return the persisted entity.
     */
    BankStatementDTO save(BankStatementDTO bankStatementDTO);

    /**
     * Get all the bankStatements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankStatementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bankStatement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankStatementDTO> findOne(Long id);

    /**
     * Delete the "id" bankStatement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
