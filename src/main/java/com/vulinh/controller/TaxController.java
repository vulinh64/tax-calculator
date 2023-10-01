package com.vulinh.controller;

import com.vulinh.model.dto.TaxDetailDTO;
import com.vulinh.model.record.TaxDetail;
import com.vulinh.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tax")
@RestController
@RequiredArgsConstructor
public class TaxController {

  private final TaxService taxService;

  @GetMapping
  public TaxDetail calculate(
      @RequestParam(defaultValue = "0") double totalSalary,
      @RequestParam(defaultValue = "0") double basicSalary,
      @RequestParam(defaultValue = "0") int numberOfDependants) {
    return taxService.calculate(
        TaxDetailDTO.builder()
            .totalSalary(totalSalary)
            .basicSalary(basicSalary)
            .numberOfDependants(numberOfDependants)
            .build());
  }
}
