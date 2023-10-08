package com.vulinh.filter;

import com.vulinh.constant.result.JwtResult;
import com.vulinh.exception.ValidationException;
import com.vulinh.security.properties.CustomAuthentication;
import com.vulinh.security.properties.SecurityConfigProperties;
import com.vulinh.utils.JwtValidationUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final SecurityConfigProperties securityConfigProperties;
  private final JwtValidationUtils jwtValidationUtils;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

      var bearerToken =
          Optional.ofNullable(authorization)
              .map(token -> token.startsWith("Bearer") ? token.substring(7) : token)
              .orElseThrow(() -> new ValidationException(JwtResult.EMPTY_AUTHORIZATION));

      var jwtPayload = jwtValidationUtils.validate(bearerToken);

      var authentication = new CustomAuthentication(jwtPayload, request);

      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (ValidationException validationException) {
      handlerExceptionResolver.resolveException(request, response, null, validationException);
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    var path = request.getServletPath();

    for (var noFilterUrl : securityConfigProperties.noFilterUrls()) {
      if (path.startsWith(noFilterUrl)) {
        return true;
      }
    }

    return false;
  }
}
