package com.vulinh.utils;

import java.io.Serializable;

public interface BaseErrorResult extends Serializable {

  String errorCode();

  String message();
}
