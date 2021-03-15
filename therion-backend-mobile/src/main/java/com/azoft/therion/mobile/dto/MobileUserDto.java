package com.azoft.therion.mobile.dto;

import com.azoft.therion.mobile.entity.MobileRoleName;
import java.util.List;
import lombok.Data;

@Data
public class MobileUserDto {
  private Long id;
  private String name;
  private String phone;
  private List<MobileRoleName> roles;
}
