package com.azoft.therion.mobile.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {
  @NotBlank
  @Size(min = 4, max = 40)
  private String name;

  @NotBlank
  @Size(min = 3, max = 15)
  private String phone;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;

}
