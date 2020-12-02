package com.bank.loanamountapprovalservice.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class DecisionRequest {

    @NotBlank(message = "Manager Id must not be blank")
    private String managerId;

    @NotBlank(message = "Customer Id must not be blank")
    private String customerId;
}
