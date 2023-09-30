package com.vulinh.utils;

import static com.vulinh.constant.CommonConstant.DEDUCTION_PER_DEPENDANTS;
import static com.vulinh.constant.CommonConstant.NON_TAXABLE_INCOME;
import static com.vulinh.constant.TaxLevel.LEVEL_0;
import static com.vulinh.constant.TaxLevel.getTaxLevel;
import static com.vulinh.constant.TaxRate.*;

import com.google.common.collect.ImmutableMap;
import com.vulinh.constant.TaxLevel;
import com.vulinh.dto.InsuranceDTO;
import com.vulinh.dto.PersonalTaxDTO;
import com.vulinh.dto.TaxDetailDTO;
import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaxUtils {

  public static InsuranceDTO calculateInsurance(TaxDetailDTO taxDetailDTO) {
    var basicSalary = taxDetailDTO.basicSalary();
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

    var totalInsurance = insuranceDTO.totalInsurance();

    // Thu nhập trước thuế = Tổng thu nhập - Bảo hiểm
    var pretaxSalary = totalSalary - totalInsurance;

    // Thu nhập chịu thuế = Thu nhập trước thuế - Thu nhập miễn thuế - Giảm trừ người phụ thuộc
    var taxableIncome =
        pretaxSalary
            - NON_TAXABLE_INCOME.value()
            // Người phụ thuộc * Giảm trừ mỗi người phụ thuộc
            - DEDUCTION_PER_DEPENDANTS.value() * taxDetailDTO.numberOfDependants();

    if (taxableIncome < 0) {
      taxableIncome = 0.0;
    }

    // Thuế lũy tiến
    var progressiveTax = progressiveTax(taxableIncome);

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

  private static Map<String, Double> progressiveTax(double taxableIncome) {
    // Tính bậc thuế
    var taxLevel = taxLevel(taxableIncome);

    var currentOrdinal = taxLevel.ordinal();
    var currentRate = taxLevel.rate();
    var currentName = taxLevel.name();

    if (taxLevel == LEVEL_0) {
      return Collections.emptyMap();
    }

    var resultBuilder = ImmutableMap.<String, Double>builder();

    for (var previousLevel = 1; previousLevel < currentOrdinal; previousLevel++) {
      var currentTaxLevel = getTaxLevel(previousLevel);

      resultBuilder.put(
          currentTaxLevel.name(),
          (currentTaxLevel.threshold() - getTaxLevel(previousLevel - 1).threshold())
              * currentTaxLevel.rate());
    }

    return resultBuilder
        .put(
            currentName,
            (taxableIncome - getTaxLevel(currentOrdinal - 1).threshold()) * currentRate)
        .build();
  }

  private static TaxLevel taxLevel(double taxableIncome) {
    var taxLevel = LEVEL_0;

    while (taxableIncome > taxLevel.threshold()) {
      taxLevel = getTaxLevel(taxLevel.ordinal() + 1);
    }

    return taxLevel;
  }
}
