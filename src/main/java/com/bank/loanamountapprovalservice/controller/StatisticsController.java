package com.bank.loanamountapprovalservice.controller;

import com.bank.loanamountapprovalservice.service.StatisticsService;
import com.bank.loanamountapprovalservice.model.Statistics;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @ResponseBody
    @GetMapping("/statistics")
    public Statistics deliverStatistics() {
        return statisticsService.collect();
    }
}
