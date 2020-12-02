package com.bank.loanamountapprovalservice.controller;

import com.bank.loanamountapprovalservice.exception.ActionNotAcceptableException;
import com.bank.loanamountapprovalservice.service.ContractService;
import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.model.ContractRequest;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
public class ContractController {
    protected Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;

    @GetMapping("/contracts")
    public List<Contract> getAllContracts() {
        return contractService.findAllContracts();
    }

    @PostMapping("/contract")
    public Contract createContract(@Valid @RequestBody ContractRequest contractRequest) {
        return contractService.save(contractRequest.toContract());
    }

    @PostMapping("/contracts")
    public ResponseEntity<List<Contract>> createContracts(@NotEmpty @RequestBody List<@Valid ContractRequest> contractRequests) {
        List<Contract> savedContracts = new ArrayList<>();
        contractRequests.forEach(contractRequest -> {
            try {
                savedContracts.add(contractService.save(contractRequest.toContract()));
            } catch (ActionNotAcceptableException e) {
                logger.error("Contract not created: {}", e.getMessage());
            }
        });

        return new ResponseEntity<>(
                savedContracts,
                allContractsSaved(contractRequests, savedContracts) ? HttpStatus.CREATED : HttpStatus.PARTIAL_CONTENT);
    }

    private boolean allContractsSaved(List<ContractRequest> contractRequests, List<Contract> savedContracts) {
        return contractRequests.size() == savedContracts.size();
    }
}
