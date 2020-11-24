package com.melendez.kata.web.rest;

import com.melendez.kata.service.BankService;
import com.melendez.kata.service.dto.BankStatementDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
     * @param statementDTO
     * @return
     */
    @PostMapping("/actions/deposit-money")
    public ResponseEntity<BankStatementDTO> depositMoney(@RequestBody @Valid BankStatementDTO statementDTO) throws URISyntaxException {
        statementDTO = bankService.depositMoney(statementDTO);
        return ResponseEntity.created(new URI("api/bank/operations/"+statementDTO.getId()))
            .body( statementDTO );
    }

    /**
     * POST withdrawMoney
     * @param statementDTO
     * @return
     */
    @PostMapping("/actions/withdraw-money")
    public ResponseEntity<BankStatementDTO> withdrawMoney(@RequestBody @Valid BankStatementDTO statementDTO) throws URISyntaxException {
        statementDTO = bankService.withdraw(statementDTO);
        return ResponseEntity.created(new URI("api/bank/operations/"+statementDTO.getId()))
            .body(statementDTO);
    }


    @GetMapping("/statements")
    public ResponseEntity<Set<BankStatementDTO>> getStatements() {
        Set<BankStatementDTO> statementDTOS = bankService.fetchStatements();
        return ResponseEntity.ok(statementDTOS);
    }

}
