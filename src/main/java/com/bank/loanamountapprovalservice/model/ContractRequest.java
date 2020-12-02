package com.bank.loanamountapprovalservice.model;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ContractRequest {

    @Pattern(regexp = "^[A-Za-z0-9]{2}+-[A-Za-z0-9]{4}+-[A-Za-z0-9]{3}$",
            message = "Customer Id must be in a pattern XX-XXXX-XXX where X is either a number or a letter")
    private String customerId;

    @NotNull(message = "Contract amount must not be blank")
    @Positive(message = "Contract amount must be a positive number")
    private BigDecimal contractAmount;

    @Size(min = 1, max = 3, message = "List should contain up to 3 approvers")
    private List<String> managerIdList;

    public Contract toContract() {
        return new Contract(customerId, contractAmount, managerIdList);
    }
}
