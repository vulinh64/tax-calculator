package com.vulinh.dto;

import java.util.Map;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record PersonalTaxDTO(
    double pretaxSalary,
    double taxableIncome,
    double taxAmount,
    Map<String, Double> progressiveTax,
    double netIncome) {}
