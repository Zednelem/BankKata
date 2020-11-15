package com.melendez.kata.web.rest;

import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.StatementDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

/**
 * BankResource controller
 */
@RestController
@RequestMapping("/api/bank")
public class BankResource {

    private final BankService bankService;

    public BankResource(BankService bankService){
        this.bankService = bankService;
    }

    /**
     * POST depositMoney
     */
    @PostMapping("/actions/deposit-money")
    public ResponseEntity<StatementDTO> depositMoney(@RequestBody @Valid StatementDTO statementDTO) throws URISyntaxException {
        statementDTO = bankService.depositMoney(statementDTO);
        return ResponseEntity.created(new URI("api/bank/operations/"+statementDTO.getId()))
            .body( statementDTO );
    }

    /**
     * POST withdrawMoney
     */
    @PostMapping("/actions/withdraw-money")
    public ResponseEntity<StatementDTO>  withdrawMoney(@RequestBody @Valid StatementDTO statementDTO) throws URISyntaxException {
        statementDTO = bankService.withdraw(statementDTO);
        return ResponseEntity.created(new URI("api/bank/operations/"+statementDTO.getId()))
            .body(statementDTO);
    }


    @GetMapping("/statements")
    public ResponseEntity<Set<StatementDTO>> getStatements() {
        Set<StatementDTO> statementDTOS = bankService.fetchStatements();
        return ResponseEntity.ok(statementDTOS);
    }

}
