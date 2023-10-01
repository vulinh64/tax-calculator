package com.vulinh.utils;

import static com.vulinh.constant.CommonConstant.DEDUCTION_PER_DEPENDANTS;
import static com.vulinh.constant.CommonConstant.NON_TAXABLE_INCOME;
import static com.vulinh.constant.TaxLevel.LEVEL_0;
import static com.vulinh.constant.TaxLevel.parseTaxLevel;
import static com.vulinh.constant.InsuranceRate.*;

import com.google.common.collect.ImmutableMap;
import com.vulinh.constant.TaxLevel;
import com.vulinh.model.dto.InsuranceDTO;
import com.vulinh.model.dto.PersonalTaxDTO;
import com.vulinh.model.dto.TaxDetailDTO;
import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaxUtils {

  public static InsuranceDTO calculateInsurance(TaxDetailDTO taxDetailDTO) {
    var basicSalary = taxDetailDTO.basicSalary();

    // Được đóng bảo hiểm khi tổng lương >= Lương đóng bảo hiểm
    var isEligibleForInsurance = taxDetailDTO.totalSalary() >= basicSalary;

    var healthInsurance = isEligibleForInsurance ? basicSalary * HEALTH_INSURANCE.rate() : 0;
    var socialInsurance = isEligibleForInsurance ? basicSalary * SOCIAL_INSURANCE.rate() : 0;
    var unemploymentInsurance =
        isEligibleForInsurance ? basicSalary * UNEMPLOYMENT_INSURANCE.rate() : 0;

    return InsuranceDTO.builder()
        .healthInsurance(healthInsurance)
        .socialInsurance(socialInsurance)
        .unemploymentInsurance(unemploymentInsurance)
        .totalInsurance(healthInsurance + socialInsurance + unemploymentInsurance)
        .build();
  }

  public static PersonalTaxDTO calculatePersonalTax(
      TaxDetailDTO taxDetailDTO, InsuranceDTO insuranceDTO) {
    var totalSalary = taxDetailDTO.totalSalary();

    // Tổng đóng BH
    var totalInsurance = insuranceDTO.totalInsurance();

    // Thu nhập trước thuế = Tổng thu nhập - Bảo hiểm
    var pretaxSalary = totalSalary - totalInsurance;

    // Thu nhập chịu thuế = Thu nhập trước thuế - Thu nhập miễn thuế - Giảm trừ người phụ thuộc
    var taxableIncome =
        pretaxSalary
            - (NON_TAXABLE_INCOME.value()
                // Người phụ thuộc * Giảm trừ mỗi người phụ thuộc
                + DEDUCTION_PER_DEPENDANTS.value() * taxDetailDTO.numberOfDependants());

    // Thu nhập chịu thuế luôn phải lớn hơn hoặc bằng 0
    if (taxableIncome < 0) {
      taxableIncome = 0.0;
    }

    // Thuế lũy tiến
    var progressiveTax = calculateProgressiveTax(taxableIncome);

    // Tổng thuế từ các bậc
    var taxAmount = progressiveTax.values().stream().mapToDouble(Double::doubleValue).sum();

    return PersonalTaxDTO.builder()
        .pretaxSalary(pretaxSalary)
        .taxableIncome(taxableIncome)
        .taxAmount(taxAmount)
        .progressiveTax(progressiveTax)
        .netIncome(totalSalary - taxAmount - totalInsurance)
        .build();
  }

  private static Map<String, Double> calculateProgressiveTax(double taxableIncome) {
    // Tính bậc thuế
    var taxLevel = calculateTaxLevel(taxableIncome);

    if (taxLevel == LEVEL_0) {
      return Collections.emptyMap();
    }

    var currentOrdinal = taxLevel.ordinal();

    var resultBuilder = ImmutableMap.<String, Double>builder();

    // Tiền thuế các bậc thuế trước
    for (var previousLevel = 1; previousLevel < currentOrdinal; previousLevel++) {
      var currentTaxLevel = parseTaxLevel(previousLevel);
      var previousTaxLevel = parseTaxLevel(previousLevel - 1);

      resultBuilder.put(
          currentTaxLevel.name(),
          (currentTaxLevel.threshold() - previousTaxLevel.threshold()) * currentTaxLevel.rate());
    }

    // Tiền thuế bậc hiện tại = (thu nhập chịu thuế - thu nhập ngưỡng bậc trước) * mức thuế (%)
    return resultBuilder
        .put(
            taxLevel.name(),
            (taxableIncome - parseTaxLevel(currentOrdinal - 1).threshold()) * taxLevel.rate())
        .build();
  }

  private static TaxLevel calculateTaxLevel(double taxableIncome) {
    var taxLevel = LEVEL_0;

    while (taxableIncome > taxLevel.threshold()) {
      taxLevel = parseTaxLevel(taxLevel.ordinal() + 1);
    }

    return taxLevel;
  }
}
