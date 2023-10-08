package com.vulinh.controller;

import com.vulinh.model.GeneralResponse;
import com.vulinh.model.record.RequestAccessToken;
import com.vulinh.utils.JwtGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtGenerationUtils jwtGenerationUtils;

  @PostMapping("/issue-jwt")
  public GeneralResponse<RequestAccessToken> issueAccessToken(
      @RequestBody RequestAccessToken requestAccessToken) {
    var payload =
        RequestAccessToken.builder()
            .accessToken(jwtGenerationUtils.generateAccessToken(requestAccessToken))
            .build();

    return GeneralResponse.success(payload);
  }
}
