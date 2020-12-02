package com.bank.loanamountapprovalservice.controller;

import com.bank.loanamountapprovalservice.service.DecisionService;
import com.bank.loanamountapprovalservice.model.DecisionRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestController
public class DecisionController {

    @Autowired
    private DecisionService decisionService;

    @PostMapping("/decisions")
    public DecisionRequest createDecision(@Valid @RequestBody DecisionRequest decisionRequest) {
         return decisionService.make(decisionRequest);
    }
}
