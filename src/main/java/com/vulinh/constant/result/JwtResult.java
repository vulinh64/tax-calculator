package com.vulinh.constant.result;

import com.vulinh.utils.BaseErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public enum JwtResult implements BaseErrorResult {
  EMPTY_AUTHORIZATION("4000", "Please specify correct authorization info"),
  EXPIRED("4001", "The session has expired"),
  INVALID("4002", "Invalid token format"),
  MALFORMED("4003", "Bad token format"),
  UNSUPPORTED("4004", "Unsupported algorithm"),
  SIGNATURE_ERROR("4005", "Unmatched signature"),
  DECODING_ERROR("4006", "JWT Decoding error"),
  TOO_SOON("4007", "Too soon to gain access");

  private final String errorCode;
  private final String message;
}
