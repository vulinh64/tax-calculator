package com.vulinh.exception;

import com.vulinh.utils.BaseErrorResult;
import java.io.Serial;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ValidationException extends RuntimeException {

  @Serial private static final long serialVersionUID = -1151980278092097911L;

  public ValidationException(BaseErrorResult errorResult) {
    this(errorResult, null);
  }

  public ValidationException(BaseErrorResult errorResult, Throwable cause) {
    super(cause);
    this.errorResult = errorResult;
  }

  private final BaseErrorResult errorResult;
}
