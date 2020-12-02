package com.bank.loanamountapprovalservice.service;

import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.model.ContractStatus;
import com.bank.loanamountapprovalservice.model.Statistics;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import com.google.common.collect.ImmutableList;
import java.math.BigDecimal;
import java.util.Collections;

import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsServiceTests {
    private static final long INTERVAL_SECONDS = 60;
    private static final LocalDateTime DATE_FOR_CALCULATION_TEST = LocalDateTime.of(2020, 12, 2, 8, 0, 0, 0);
    private static final LocalDateTime DATE_FOR_EMPTY_TEST = DATE_FOR_CALCULATION_TEST.plusDays(1);
    private static final Contract CONTRACT_1000 = new Contract("aa-test-123", new BigDecimal(1000), ImmutableList.of("man1", "man2", "man3"));
    private static final Contract CONTRACT_3000 = new Contract("bb-test-123", new BigDecimal(3000), ImmutableList.of("man4", "man5", "man6"));

    private static StatisticsService statisticsService;
    private static ContractService contractService;
    private static MockedStatic<LocalDateTime> mockedLocalDateTime;

    @BeforeAll
    public static void setup() {
        mockedLocalDateTime = mockStatic(LocalDateTime.class);
        contractService = mock(ContractService.class);
        statisticsService = new StatisticsService(contractService, INTERVAL_SECONDS);
    }

    @BeforeEach
    public void prepareStatisticsData() {
        mockedLocalDateTime.reset();
    }

    @Test
    void collectMultiStatisticsTest() {
        when(contractService.findContractsAfterDateWithStatus(eq(DATE_FOR_CALCULATION_TEST), any(ContractStatus.class)))
                .thenReturn(ImmutableList.of(CONTRACT_1000, CONTRACT_3000));
        Statistics expected = Statistics.builder()
                .statisticsFrom(DATE_FOR_CALCULATION_TEST).approvedLoansCount(2)
                .totalLoansAmount(new BigDecimal(4000)).averageLoanAmount(new BigDecimal("2000.00"))
                .maxLoanAmount(new BigDecimal(3000)).minLoanAmount(new BigDecimal(1000))
                .build();
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(DATE_FOR_CALCULATION_TEST.plusSeconds(INTERVAL_SECONDS));
        assertEquals(expected, statisticsService.collect());
    }

    @Test
    void collectEmptyStatisticsTest() {
        when(contractService.findContractsAfterDateWithStatus(eq(DATE_FOR_EMPTY_TEST), any(ContractStatus.class)))
                .thenReturn(Collections.emptyList());
        Statistics expected = Statistics.builder()
                .statisticsFrom(DATE_FOR_CALCULATION_TEST).approvedLoansCount(0)
                .totalLoansAmount(null).averageLoanAmount(null)
                .maxLoanAmount(null).minLoanAmount(null)
                .build();
        mockedLocalDateTime.when(LocalDateTime::now).thenReturn(DATE_FOR_CALCULATION_TEST.plusSeconds(INTERVAL_SECONDS));
        assertEquals(expected, statisticsService.collect());
    }
}
