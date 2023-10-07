package com.vulinh.utils;

import io.jsonwebtoken.io.Decoders;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

  private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
  private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

  @SneakyThrows
  public static PublicKey generatePublicKey(String rawPublicKey, String algorithm) {
    // Remove header and footer if they are present
    // Also remove any whitespace character (\n, \r, space)
    var refinedPublicKey =
        StringUtils.deleteWhitespace(
            rawPublicKey
                .replace(BEGIN_PUBLIC_KEY, StringUtils.EMPTY)
                .replace(END_PUBLIC_KEY, StringUtils.EMPTY));

    var keySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(refinedPublicKey));

    return KeyFactory.getInstance(algorithm).generatePublic(keySpec);
  }
}
