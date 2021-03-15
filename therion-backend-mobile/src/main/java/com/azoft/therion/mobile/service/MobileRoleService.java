package com.azoft.therion.mobile.service;

import com.azoft.therion.mobile.entity.MobileRole;
import com.azoft.therion.mobile.entity.MobileRoleName;
import com.azoft.therion.mobile.repository.MobileRoleRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MobileRoleService {

  private final MobileRoleRepository mobileRoleRepository;

  public MobileRoleService(MobileRoleRepository mobileRoleRepository) {
    this.mobileRoleRepository = mobileRoleRepository;
  }

  @Transactional
  public Optional<MobileRole> findByName(MobileRoleName name) {
    return mobileRoleRepository.findByName(name);
  }
}
