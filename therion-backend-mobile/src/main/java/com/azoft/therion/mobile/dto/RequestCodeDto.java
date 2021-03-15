package com.azoft.therion.mobile.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCodeDto {

  @NotBlank
  private String phone;
}
