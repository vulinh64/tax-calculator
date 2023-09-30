package com.vulinh.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@Builder
@With
public record Insurance(
    @JsonProperty("BHXH") BigDecimal socialInsurance,
    @JsonProperty("BHYT") BigDecimal healthInsurance,
    @JsonProperty("BH thất nghiệp") BigDecimal unemploymentInsurance,
    @JsonProperty("Tổng đóng BH") BigDecimal totalInsurance) {}
