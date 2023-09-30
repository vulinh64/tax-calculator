package com.vulinh.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record InsuranceDTO(
    double socialInsurance,
    double healthInsurance,
    double unemploymentInsurance,
    double totalInsurance) {}
