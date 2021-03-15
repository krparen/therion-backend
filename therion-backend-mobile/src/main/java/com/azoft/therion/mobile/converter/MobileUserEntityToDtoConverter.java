package com.azoft.therion.mobile.converter;

import com.azoft.therion.mobile.dto.MobileUserDto;
import com.azoft.therion.mobile.entity.MobileRole;
import com.azoft.therion.mobile.entity.MobileRoleName;
import com.azoft.therion.mobile.entity.MobileUser;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MobileUserEntityToDtoConverter implements Converter<MobileUser, MobileUserDto> {

  @Override
  public MobileUserDto convert(MobileUser input) {
    MobileUserDto result = new MobileUserDto();
    result.setId(input.getId());
    result.setName(input.getName());
    result.setPhone(input.getPhone());

    List<MobileRoleName> roleNames = input.getMobileRoles().stream()
        .map(MobileRole::getName)
        .collect(Collectors.toList());

    result.setRoles(roleNames);

    return result;
  }
}
