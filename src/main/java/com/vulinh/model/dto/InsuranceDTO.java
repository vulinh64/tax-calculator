package com.vulinh.model.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record InsuranceDTO(
    double socialInsurance,
    double healthInsurance,
    double unemploymentInsurance,
    double totalInsurance) {}
