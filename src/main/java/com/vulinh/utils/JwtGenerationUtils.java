package com.vulinh.utils;

import com.vulinh.model.record.RequestAccessToken;
import com.vulinh.security.properties.SecurityConfigProperties;
import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtGenerationUtils {

  private final SecurityConfigProperties securityConfigProperties;

  @SneakyThrows
  public String generateAccessToken(RequestAccessToken requestAccessToken) {
    var issueDateTime = LocalDateTime.now();

    return Jwts.builder()
        .setSubject(requestAccessToken.username())
        .setAudience(requestAccessToken.audiences())
        .setIssuer(securityConfigProperties.defaultIssuer())
        .setIssuedAt(toDate(issueDateTime))
        .setExpiration(toDate(issueDateTime.plus(securityConfigProperties.jwtDuration())))
        .addClaims(toClaims(requestAccessToken))
        .signWith(SecurityUtils.generatePrivateKey(securityConfigProperties.privateKey()))
        .serializeToJsonWith(JwtGenerationUtils::serialize)
        .compact();
  }

  private Map<String, Object> toClaims(RequestAccessToken requestAccessToken) {
    return Map.of("role", requestAccessToken.roles());
  }

  private static Date toDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  @SneakyThrows
  private static byte[] serialize(Map<String, ?> payload) {
    return JsonUtils.delegate().writeValueAsBytes(payload);
  }
}
