package com.bank.loanamountapprovalservice.service;

import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.model.ContractRequest;
import com.bank.loanamountapprovalservice.model.ContractStatus;
import com.bank.loanamountapprovalservice.model.DecisionRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class ContractServiceTests {
    private static final UUID DEFAULT_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    private static final LocalDateTime DEFAULT_LOCAL_DATE_TIME = LocalDateTime.of(2020, 12, 2, 8, 0, 0, 0);
    private static final ContractRequest CONTRACT_REQUEST = new ContractRequest("aa-test-123", new BigDecimal(1000), ImmutableList.of("man1"));
    private static final DecisionRequest DECISION_APPPROVAL = new DecisionRequest("man3", "bb-test-999");
    private static final Contract CONTRACT_APPROVAL = new Contract("bb-test-999", new BigDecimal(1000), ImmutableList.of("man3"));

    private static ContractService contractService;
    private static List<Contract> contracts;
    private static MockedStatic<LocalDateTime> mockedLocalDateTime;

    @BeforeAll
    public static void setup() {
        mockStatic(UUID.class).when(UUID::randomUUID).thenReturn(DEFAULT_UUID);
        mockedLocalDateTime = mockStatic(LocalDateTime.class);
        contracts = new ArrayList<>();
        contractService = new ContractService(contracts);
    }

    @Test
    void contractSavingTest() {
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(DEFAULT_LOCAL_DATE_TIME);
        Contract expectedContract = new Contract(CONTRACT_REQUEST.getCustomerId(), CONTRACT_REQUEST.getContractAmount(), CONTRACT_REQUEST.getManagerIdList());
        assertEquals(expectedContract, contractService.save(CONTRACT_REQUEST.toContract()));
        assertTrue(contracts.contains(expectedContract));
    }

    @Test
    void contractApprovalTest() {
        Contract approvedContract = contractService.approveContract(CONTRACT_APPROVAL, DECISION_APPPROVAL.getManagerId());
        assertEquals(ContractStatus.APPROVED, approvedContract.getContractStatus());
    }
}
