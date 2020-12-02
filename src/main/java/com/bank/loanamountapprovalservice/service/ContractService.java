package com.bank.loanamountapprovalservice.service;

import com.bank.loanamountapprovalservice.exception.ActionNotAcceptableException;
import com.bank.loanamountapprovalservice.model.Contract;
import com.bank.loanamountapprovalservice.model.ContractStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.bank.loanamountapprovalservice.model.ContractStatus.APPROVED;
import static com.bank.loanamountapprovalservice.model.ContractStatus.PENDING;
import static java.util.stream.Collectors.toList;

@Component
public class ContractService {
    protected Logger logger = LoggerFactory.getLogger(ContractService.class);

    private final List<Contract> contracts;

    public ContractService(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<Contract> findAllContracts() {
        return contracts;
    }

    public List<Contract> findContractsByCustomerId(String customerId) {
        return contracts.stream().filter(contract -> contract.getCustomerId().equals(customerId)).collect(toList());
    }

    public List<Contract> findContractsByCustomerIdAndStatus(String customerId, ContractStatus status) {
        return findContractsByCustomerId(customerId).stream().filter(contract -> contract.getContractStatus().equals(status)).collect(toList());
    }

    public List<Contract> findContractsAfterDate(LocalDateTime dateTime) {
        return contracts.stream().filter(contract -> contract.getContractStatusTs().isAfter(dateTime)).collect(toList());
    }

    public List<Contract> findContractsAfterDateWithStatus(LocalDateTime dateTime, ContractStatus status) {
        return findContractsAfterDate(dateTime).stream().filter(contract -> contract.getContractStatus().equals(status)).collect(toList());
    }

    public Contract approveContract(Contract contract, String managerId) {
        contract.getApprovalsMap().replace(managerId, true);
        if (!contract.getApprovalsMap().containsValue(false)) {
            updateStatus(contract, APPROVED);
        }
        return contract;
    }

    private Contract updateStatus(Contract contract, ContractStatus status) {
        contract.setContractStatus(status);
        logger.info("Status changed ( contractId: {}, contractStatus: {} )", contract.getContractId(), contract.getContractStatus());
        logger.info("Sending to customerId: {}...", contract.getCustomerId());
        return contract;
    }

    public Contract save(Contract contract) {
        if (hasCustomerHavePendingContracts(contract.getCustomerId())) {
            throw new ActionNotAcceptableException("Contract with status PENDING already exist for customerId: " + contract.getCustomerId());
        }
        contracts.add(contract);

        logger.info("New contract received ( contractId: {}, customerId: {}, amount: {}, approvers: {}, status: {} )",
                contract.getContractId(), contract.getCustomerId(), contract.getContractAmount(), contract.getManagerIdList(), contract.getContractStatus());

        return contract;
    }

    private boolean hasCustomerHavePendingContracts(String customerId) {
        return findContractsByCustomerId(customerId).stream().anyMatch(contract -> PENDING.equals(contract.getContractStatus()));
    }
}
