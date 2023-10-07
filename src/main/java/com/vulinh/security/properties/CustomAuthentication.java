package com.vulinh.security.properties;

import com.vulinh.utils.JwtPayload;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serial;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomAuthentication extends AbstractAuthenticationToken {

  @Serial private static final long serialVersionUID = 7470565413815315182L;

  private final JwtPayload principal;

  public CustomAuthentication(JwtPayload principal, HttpServletRequest httpServletRequest) {
    super(principal.toAuthority());
    this.principal = principal;
    setDetails(new WebAuthenticationDetails(httpServletRequest));
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }
}
