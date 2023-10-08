package com.vulinh.utils;

import static com.vulinh.constant.result.JwtResult.*;
import static com.vulinh.constant.result.ResultResult.UNKNOWN_ERROR;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vulinh.exception.ValidationException;
import com.vulinh.security.properties.SecurityConfigProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtValidationUtils {

  public JwtPayload validate(String token) {
    if (StringUtils.isBlank(token)) {
      throw new ValidationException(EMPTY_AUTHORIZATION);
    }

    try {
      var parsingResult = getJwtParser().parseClaimsJws(token).getBody();

      return JsonUtils.delegate().convertValue(parsingResult, JwtPayload.class);
    } catch (ExpiredJwtException expiredJwtException) {
      return sendLogAndThrow(expiredJwtException, EXPIRED);
    } catch (MalformedJwtException malformedJwtException) {
      return sendLogAndThrow(malformedJwtException, MALFORMED);
    } catch (UnsupportedJwtException unsupportedJwtException) {
      return sendLogAndThrow(unsupportedJwtException, UNSUPPORTED);
    } catch (DecodingException decodingException) {
      return sendLogAndThrow(decodingException, DECODING_ERROR);
    } catch (SignatureException signatureException) {
      return sendLogAndThrow(signatureException, SIGNATURE_ERROR);
    } catch (IllegalArgumentException illegalArgumentException) {
      return sendLogAndThrow(illegalArgumentException, INVALID);
    } catch (PrematureJwtException prematureJwtException) {
      return sendLogAndThrow(prematureJwtException, TOO_SOON);
    } catch (Exception exception) {
      throw new ValidationException(UNKNOWN_ERROR, exception);
    }
  }

  private static JwtParser jwtParser;

  // Single initialization
  private static JwtParser getJwtParser() {
    try {
      if (Objects.isNull(jwtParser)) {
        var properties = StaticContextAccessor.getBean(SecurityConfigProperties.class);

        var publicKey = SecurityUtils.generatePublicKey(properties.publicKey());

        jwtParser =
            Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .deserializeJsonWith(JwtValidationUtils::deserialize)
                .build();
      }

      return jwtParser;
    } catch (Exception exception) {
      throw new DecodingException("Invalid public key");
    }
  }

  private static Map<String, ?> deserialize(byte[] json) {
    try {
      return JsonUtils.delegate().readValue(json, new TypeReference<>() {});
    } catch (Exception ioException) {
      log.info("Invalid byte data", ioException);
      throw new DecodingException(null);
    }
  }

  // Return value to terminate catch block
  private static JwtPayload sendLogAndThrow(Throwable throwable, BaseErrorResult response) {
    log.info("JWT validation error", throwable);
    throw new ValidationException(response);
  }
}
