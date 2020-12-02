package com.bank.loanamountapprovalservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@ToString
public class Contract {

    private final UUID contractId;
    private final String customerId;
    private final BigDecimal contractAmount;
    private final List<String> managerIdList;
    private Map<String, Boolean> approvalsMap;
    private ContractStatus contractStatus;
    private LocalDateTime contractStatusTs;

    public Contract(String customerId, BigDecimal contractAmount, List<String> managerIdList) {
        this.customerId = customerId;
        this.contractAmount = contractAmount;
        this.managerIdList = managerIdList;

        contractId = UUID.randomUUID();
        approvalsMap = new HashMap<>();
        managerIdList.forEach(id -> approvalsMap.put(id, false));
        contractStatus = ContractStatus.PENDING;
        contractStatusTs = LocalDateTime.now();
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
        contractStatusTs = LocalDateTime.now();
    }
}
