package com.bank.loanamountapprovalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class FieldErrorResponse {

    private String targetField;
    private String errorMessage;
}
