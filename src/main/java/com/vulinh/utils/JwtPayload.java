package com.vulinh.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.With;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;

@Builder
@With
public record JwtPayload(
    @JsonProperty("sub") String subject,
    @JsonProperty("aud") Object audience,
    @JsonProperty("iss") String issuer,
    Collection<String> roles)
    implements Serializable {

  public JwtPayload(String subject, Object audience, String issuer, Collection<String> roles) {
    this.subject = subject;
    this.audience = audience;
    this.issuer = issuer;
    this.roles = CollectionUtils.emptyIfNull(roles);
  }

  public List<GrantedAuthority> toAuthority() {
    return roles.stream().map(JwtPayload::toAuthority).toList();
  }

  private static GrantedAuthority toAuthority(String role) {
    return () -> role;
  }
}
