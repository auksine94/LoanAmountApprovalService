package com.bank.loanamountapprovalservice.service;

import com.bank.loanamountapprovalservice.model.DecisionRequest;
import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.model.ContractStatus;
import com.bank.loanamountapprovalservice.exception.ActionNotAcceptableException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import com.google.common.collect.ImmutableList;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecisionServiceTests {
    private static final DecisionRequest DECISION_REQUEST = new DecisionRequest("man1", "aa-test-123");
    private static final Contract CONTRACT_SUCCESS = new Contract("aa-test-123", new BigDecimal(1000), ImmutableList.of("man1"));
    private static final Contract CONTRACT_NO_MANAGER = new Contract("aa-test-123", new BigDecimal(1000), ImmutableList.of("man2"));

    private static DecisionService decisionService;
    private static ContractService contractService;

    @BeforeAll
    public static void setup() {
        contractService = mock(ContractService.class);
        decisionService = new DecisionService(contractService);
    }

    @Test
    void decisionMakingSuccessTest() {
        when(contractService.findContractsByCustomerIdAndStatus(eq(DECISION_REQUEST.getCustomerId()), any(ContractStatus.class)))
                .thenReturn(ImmutableList.of(CONTRACT_SUCCESS));
        assertEquals(DECISION_REQUEST, decisionService.make(DECISION_REQUEST));
    }

    @Test
    void noContractsForCustomerTest() {
        when(contractService.findContractsByCustomerIdAndStatus(eq(DECISION_REQUEST.getCustomerId()), any(ContractStatus.class)))
                .thenReturn(Collections.emptyList());
        assertThrows(ActionNotAcceptableException.class, () -> decisionService.make(DECISION_REQUEST));
    }

    @Test
    void noContractsForManagerTest() {
        when(contractService.findContractsByCustomerIdAndStatus(eq(DECISION_REQUEST.getCustomerId()), any(ContractStatus.class)))
                .thenReturn(ImmutableList.of(CONTRACT_NO_MANAGER));
        assertThrows(ActionNotAcceptableException.class, () -> decisionService.make(DECISION_REQUEST));
    }
}
