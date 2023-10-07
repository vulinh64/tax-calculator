package com.vulinh.constant.result;

import com.vulinh.utils.BaseErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ResultResult implements BaseErrorResult {
  OK("000", ""),
  UNKNOWN_ERROR("500", "Unknown error, please refer to log for more detail");

  private final String errorCode;
  private final String message;
}
