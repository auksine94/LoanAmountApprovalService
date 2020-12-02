package com.bank.loanamountapprovalservice.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class Statistics {

    private final LocalDateTime statisticsFrom;
    private final int approvedLoansCount;
    private final BigDecimal totalLoansAmount;
    private final BigDecimal averageLoanAmount;
    private final BigDecimal maxLoanAmount;
    private final BigDecimal minLoanAmount;
}
