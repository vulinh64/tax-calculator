package com.vulinh.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spring.security.jwt")
public record SecurityConfigProperties(
    String publicKey,
    String privateKey,
    List<String> noAuthenticatedUrls,
    List<String> noFilterUrls) {

  public String[] noAuthenticatedUrlArray() {
    return noAuthenticatedUrls.toArray(String[]::new);
  }
}
