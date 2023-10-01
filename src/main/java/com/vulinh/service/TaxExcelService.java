package com.vulinh.service;

import static com.vulinh.constant.CommonConstant.DEDUCTION_PER_DEPENDANTS;
import static com.vulinh.constant.CommonConstant.NON_TAXABLE_INCOME;

import com.vulinh.model.dto.TaxDetailDTO;
import com.vulinh.model.record.TaxDetail;
import com.vulinh.utils.CellBuilder;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@RequiredArgsConstructor
public class TaxExcelService {

  private static final int VALUE_COLUMN_INDEX = 3;

  private final TaxService taxService;

  public void exportExcel(
      double totalSalary,
      double basicSalary,
      int numberOfDependants,
      HttpServletResponse httpServletResponse) {
    exportExcel(
        taxService.calculate(
            TaxDetailDTO.builder()
                .totalSalary(totalSalary)
                .basicSalary(basicSalary)
                .numberOfDependants(numberOfDependants)
                .build()),
        httpServletResponse);
  }

  @SneakyThrows
  private void exportExcel(TaxDetail taxDetail, HttpServletResponse httpServletResponse) {
    try (var inputStream = new FileInputStream(ResourceUtils.getFile("classpath:template.xlsx"));
        var workbook = new XSSFWorkbook(inputStream)) {
      var sheet = workbook.getSheetAt(0);

      // Row count
      var count = new AtomicInteger(1);

      var cellBuilder = CellBuilder.init(workbook, sheet);

      // Tổng thu nhập
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(taxDetail.totalSalary().doubleValue());

      // Lương cơ bản
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(taxDetail.basicSalary().doubleValue());

      // Số người phụ thuộc
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(taxDetail.numberOfDependants());

      // Giảm trừ cho người phụ thuộc
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(DEDUCTION_PER_DEPENDANTS.value() * taxDetail.numberOfDependants());

      // Thu nhập miễn thuế
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(NON_TAXABLE_INCOME.value());

      count.getAndIncrement();
      var insurance = taxDetail.insurance();

      // Tổng tiền bảo hiểm
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(insurance.totalInsurance().doubleValue());

      // BHYT
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(insurance.healthInsurance().doubleValue());

      // BHXH
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(insurance.socialInsurance().doubleValue());

      // BH thất nghiệp
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(insurance.unemploymentInsurance().doubleValue());

      count.getAndIncrement();
      var personalTax = taxDetail.personalTax();

      // Thuế TNCN
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(personalTax.taxAmount().doubleValue());

      // Thu nhập trước thuế
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(personalTax.pretaxSalary().doubleValue());

      // Thu nhập tính thuế
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(personalTax.taxableIncome().doubleValue());

      count.getAndIncrement();

      // Thực lĩnh
      cellBuilder
          .newCell(count.getAndIncrement(), VALUE_COLUMN_INDEX)
          .setCellValue(personalTax.netIncome().doubleValue());

      workbook.write(httpServletResponse.getOutputStream());
    }
  }
}
