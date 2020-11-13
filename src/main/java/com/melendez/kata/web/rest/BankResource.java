package com.melendez.kata.web.rest;

import com.melendez.kata.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

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
    @PostMapping("/deposit-money")
    public ResponseEntity<String> depositMoney(@RequestBody String amount) throws URISyntaxException {
        String operationId = bankService.depositMoney(Double.parseDouble(amount));
        return ResponseEntity.created(new URI("api/bank/operations/"+operationId))
            .body("\"" + operationId +"\"");
    }

    /**
    * POST withdrawMoney
    */
    @PostMapping("/withdraw-money")
    public ResponseEntity<String>  withdrawMoney(@RequestBody String amount) throws URISyntaxException {
        String operationId = bankService.withdraw(Double.parseDouble(amount));
        return ResponseEntity.created(new URI("api/bank/operations/"+operationId))
            .body("\"" + operationId +"\"");
    }

    /**
    * GET getOperationList
    */
    @GetMapping("/get-operation-list")
    public String getOperationList() {
        return "getOperationList";
    }

}
