package com.bank.loanamountapprovalservice.service;

import com.bank.loanamountapprovalservice.model.DecisionRequest;
import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.exception.ActionNotAcceptableException;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static com.bank.loanamountapprovalservice.model.ContractStatus.PENDING;

@Component
public class DecisionService {
    protected Logger logger = LoggerFactory.getLogger(DecisionService.class);

    private final ContractService contractService;

    public DecisionService(ContractService contractService) {
        this.contractService = contractService;
    }

    public DecisionRequest make(DecisionRequest decisionRequest) {
        List<Contract> customerPendingContracts = contractService.findContractsByCustomerIdAndStatus(decisionRequest.getCustomerId(), PENDING);
        if (customerPendingContracts.isEmpty()) {
            throw new ActionNotAcceptableException("No contracts exist for customerId: " + decisionRequest.getCustomerId());
        }

        //Should be only one
        Contract customerContract = customerPendingContracts.get(0);
        if (!customerContract.getApprovalsMap().containsKey(decisionRequest.getManagerId())) {
            throw new ActionNotAcceptableException("No contracts exist for managerId: " + decisionRequest.getManagerId());
        }

        contractService.approveContract(customerContract, decisionRequest.getManagerId());
        logger.info("Decision made ( contractId: {},  managerId: {} )", customerContract.getContractId(), decisionRequest.getManagerId());
        return decisionRequest;
    }
}
