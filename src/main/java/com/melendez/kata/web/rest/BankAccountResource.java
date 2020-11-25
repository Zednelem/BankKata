package com.melendez.kata.web.rest;

import com.melendez.kata.service.BankAccountService;
import com.melendez.kata.web.rest.errors.BadRequestAlertException;
import com.melendez.kata.service.dto.BankAccountDTO;
import com.melendez.kata.service.dto.BankAccountCriteria;
import com.melendez.kata.service.BankAccountQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.melendez.kata.domain.BankAccount}.
 */
@RestController
@RequestMapping("/api")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private final BankAccountService bankAccountService;

    private final BankAccountQueryService bankAccountQueryService;

    public BankAccountResource(BankAccountService bankAccountService, BankAccountQueryService bankAccountQueryService) {
        this.bankAccountService = bankAccountService;
        this.bankAccountQueryService = bankAccountQueryService;
    }

    /**
     * {@code GET  /bank-accounts} : get all the bankAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankAccounts in body.
     */
    @GetMapping("/bank-accounts")
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(BankAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BankAccounts by criteria: {}", criteria);
        Page<BankAccountDTO> page = bankAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-accounts/count} : count all the bankAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bank-accounts/count")
    public ResponseEntity<Long> countBankAccounts(BankAccountCriteria criteria) {
        log.debug("REST request to count BankAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bank-accounts/:id} : get the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-accounts/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable Long id) {
        log.debug("REST request to get BankAccount : {}", id);
        Optional<BankAccountDTO> bankAccountDTO = bankAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankAccountDTO);
    }
}
