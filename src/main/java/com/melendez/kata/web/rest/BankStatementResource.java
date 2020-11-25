package com.melendez.kata.web.rest;

import com.melendez.kata.service.BankStatementService;
import com.melendez.kata.web.rest.errors.BadRequestAlertException;
import com.melendez.kata.service.dto.BankStatementDTO;
import com.melendez.kata.service.dto.BankStatementCriteria;
import com.melendez.kata.service.BankStatementQueryService;

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
 * REST controller for managing {@link com.melendez.kata.domain.BankStatement}.
 */
@RestController
@RequestMapping("/api")
public class BankStatementResource {

    private final Logger log = LoggerFactory.getLogger(BankStatementResource.class);

    private final BankStatementService bankStatementService;

    private final BankStatementQueryService bankStatementQueryService;

    public BankStatementResource(BankStatementService bankStatementService, BankStatementQueryService bankStatementQueryService) {
        this.bankStatementService = bankStatementService;
        this.bankStatementQueryService = bankStatementQueryService;
    }

    /**
     * {@code GET  /bank-statements} : get all the bankStatements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankStatements in body.
     */
    @GetMapping("/bank-statements")
    public ResponseEntity<List<BankStatementDTO>> getAllBankStatements(BankStatementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BankStatements by criteria: {}", criteria);
        Page<BankStatementDTO> page = bankStatementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-statements/count} : count all the bankStatements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bank-statements/count")
    public ResponseEntity<Long> countBankStatements(BankStatementCriteria criteria) {
        log.debug("REST request to count BankStatements by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankStatementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bank-statements/:id} : get the "id" bankStatement.
     *
     * @param id the id of the bankStatementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankStatementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-statements/{id}")
    public ResponseEntity<BankStatementDTO> getBankStatement(@PathVariable Long id) {
        log.debug("REST request to get BankStatement : {}", id);
        Optional<BankStatementDTO> bankStatementDTO = bankStatementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankStatementDTO);
    }
}
