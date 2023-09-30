package com.vulinh.controller;

import com.vulinh.dto.TaxDetailDTO;
import com.vulinh.model.TaxDetail;
import com.vulinh.utils.TaxUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tax")
@RestController
public class TaxController {

  @GetMapping
  public TaxDetail calculate(
      @RequestParam(defaultValue = "0") double totalSalary,
      @RequestParam(defaultValue = "0") double basicSalary,
      @RequestParam(defaultValue = "0") int numberOfDependants) {
    return TaxUtils.calculate(
        TaxDetailDTO.builder()
            .totalSalary(totalSalary)
            .basicSalary(basicSalary)
            .numberOfDependants(numberOfDependants)
            .build());
  }
}
