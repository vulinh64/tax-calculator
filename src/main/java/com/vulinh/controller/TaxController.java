package com.vulinh.controller;

import com.vulinh.model.GeneralResponse;
import com.vulinh.model.dto.TaxDetailDTO;
import com.vulinh.model.record.TaxDetail;
import com.vulinh.service.TaxExcelService;
import com.vulinh.service.TaxService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tax")
@RestController
@RequiredArgsConstructor
public class TaxController {

  private final TaxService taxService;
  private final TaxExcelService taxExcelService;

  @GetMapping
  public GeneralResponse<TaxDetail> calculate(
      @RequestParam(defaultValue = "0") double totalSalary,
      @RequestParam(defaultValue = "0") double basicSalary,
      @RequestParam(defaultValue = "0") int numberOfDependants) {
    return GeneralResponse.success(
        taxService.calculate(
            TaxDetailDTO.builder()
                .totalSalary(totalSalary)
                .basicSalary(basicSalary)
                .numberOfDependants(numberOfDependants)
                .build()));
  }

  @GetMapping("/export")
  public void export(
      @RequestParam(defaultValue = "0") double totalSalary,
      @RequestParam(defaultValue = "0") double basicSalary,
      @RequestParam(defaultValue = "0") int numberOfDependants,
      HttpServletResponse httpServletResponse) {
    httpServletResponse.setHeader(
        "Content-Disposition",
        ContentDisposition.attachment()
            .filename(
                "export-%s.xlsx"
                    .formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))))
            .build()
            .toString());
    httpServletResponse.setContentType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    taxExcelService.exportExcel(totalSalary, basicSalary, numberOfDependants, httpServletResponse);
  }
}
