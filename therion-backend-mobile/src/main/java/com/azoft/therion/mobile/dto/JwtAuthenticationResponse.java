package com.azoft.therion.mobile.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
  private String accessToken;
  private MobileUserDto user;

  public JwtAuthenticationResponse(String accessToken, MobileUserDto user) {
    this.accessToken = accessToken;
    this.user = user;
  }
}
