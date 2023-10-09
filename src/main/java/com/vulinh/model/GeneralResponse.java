package com.vulinh.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vulinh.constant.result.CommonResponseResult;
import com.vulinh.utils.BaseErrorResult;
import lombok.Builder;
import lombok.With;
import org.springframework.lang.Nullable;

@Builder
@With
@JsonInclude(Include.NON_NULL)
public record GeneralResponse<T>(String errorCode, String message, T data) {

  public static <T> GeneralResponse<T> success(T data) {
    return of(CommonResponseResult.OK, data);
  }

  public static GeneralResponse<Object> error(BaseErrorResult baseErrorResult) {
    return of(baseErrorResult, null);
  }

  private static <T> GeneralResponse<T> of(BaseErrorResult baseErrorResult, @Nullable T data) {
    return GeneralResponse.<T>builder()
        .errorCode(baseErrorResult.errorCode())
        .message(baseErrorResult.message())
        .data(data)
        .build();
  }
}
