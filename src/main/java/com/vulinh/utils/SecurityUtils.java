package com.vulinh.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SecurityUtils {

  private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
  private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
  private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
  private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

  private static final KeyFactory KEY_FACTORY;

  static {
    try {
      log.info("Using RSA as default algorithm for KeyFactory");
      KEY_FACTORY = KeyFactory.getInstance("RSA");
    } catch (Exception exception) {
      throw new ExceptionInInitializerError(exception);
    }
  }

  private static PublicKey singletonPublicKey;
  private static PrivateKey singletonPrivateKey;

  @SneakyThrows
  public static PublicKey generatePublicKey(String rawPublicKey) {
    if (Objects.isNull(singletonPublicKey)) {
      // Remove header and footer if they are present
      // Also remove any whitespace character (\n, \r, space)
      var refinedPrivateKey =
          StringUtils.deleteWhitespace(
              rawPublicKey
                  .replace(BEGIN_PUBLIC_KEY, StringUtils.EMPTY)
                  .replace(END_PUBLIC_KEY, StringUtils.EMPTY));

      var keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(refinedPrivateKey));

      singletonPublicKey = KEY_FACTORY.generatePublic(keySpec);
    }

    return singletonPublicKey;
  }

  @SneakyThrows
  public static PrivateKey generatePrivateKey(String rawPrivateKey) {
    if (Objects.isNull(singletonPrivateKey)) {
      // Remove header and footer if they are present
      // Also remove any whitespace character (\n, \r, space)
      var refinedPrivateKey =
          StringUtils.deleteWhitespace(
              rawPrivateKey
                  .replace(BEGIN_PRIVATE_KEY, StringUtils.EMPTY)
                  .replace(END_PRIVATE_KEY, StringUtils.EMPTY));

      var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(refinedPrivateKey));

      singletonPrivateKey = KEY_FACTORY.generatePrivate(keySpec);
    }

    return singletonPrivateKey;
  }
}
