package com.bank.loanamountapprovalservice.service;

import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.model.ContractStatus;
import com.bank.loanamountapprovalservice.model.Statistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class StatisticsService {
    private final ContractService contractService;
    private final long defaultStatisticsIntervalSeconds;

    public StatisticsService(ContractService contractService, @Value("${default.statistics.interval.seconds}") long defaultStatisticsIntervalSeconds) {
        this.contractService = contractService;
        this.defaultStatisticsIntervalSeconds = defaultStatisticsIntervalSeconds;
    }

    public Statistics collect() {
        LocalDateTime statisticsFrom = LocalDateTime.now().minusSeconds(defaultStatisticsIntervalSeconds);
        List<Contract> contracts = contractService.findContractsAfterDateWithStatus(statisticsFrom, ContractStatus.APPROVED);
        return Statistics.builder()
                .statisticsFrom(statisticsFrom)
                .approvedLoansCount(contracts.size())
                .totalLoansAmount(findTotalLoansAmount(contracts))
                .averageLoanAmount(findAverageLoansAmount(contracts))
                .maxLoanAmount(findMaximumLoansAmount(contracts))
                .minLoanAmount(findMinimumLoansAmount(contracts))
                .build();
    }

    private BigDecimal findTotalLoansAmount(List<Contract> contracts) {
        return contracts.stream().map(Contract::getContractAmount).reduce(BigDecimal::add).orElse(null);
    }

    private BigDecimal findAverageLoansAmount(List<Contract> contracts) {
        long count = contracts.size();
        BigDecimal sum = findTotalLoansAmount(contracts);
        if (count == 0 || Objects.isNull(sum)) {
            return null;
        }
        return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal findMaximumLoansAmount(List<Contract> contracts) {
        return contracts.stream().map(Contract::getContractAmount).max(BigDecimal::compareTo).orElse(null);
    }

    private BigDecimal findMinimumLoansAmount(List<Contract> contracts) {
        return contracts.stream().map(Contract::getContractAmount).min(BigDecimal::compareTo).orElse(null);
    }
}
