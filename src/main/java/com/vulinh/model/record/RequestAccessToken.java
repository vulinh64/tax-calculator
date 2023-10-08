package com.vulinh.model.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collection;
import lombok.Builder;
import lombok.With;
import org.apache.commons.collections4.CollectionUtils;

@JsonInclude(Include.NON_NULL)
@Builder
@With
public record RequestAccessToken(
    String username, String audiences, Collection<String> roles, String accessToken) {
  public RequestAccessToken(
      String username, String audiences, Collection<String> roles, String accessToken) {
    this.username = username;
    this.audiences = audiences;
    this.roles = CollectionUtils.emptyIfNull(roles);
    this.accessToken = accessToken;
  }
}
