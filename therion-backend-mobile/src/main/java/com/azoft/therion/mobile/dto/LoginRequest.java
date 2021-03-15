package com.azoft.therion.mobile.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

  @NotBlank
  private String phone;

  @NotBlank
  private String password;
}
