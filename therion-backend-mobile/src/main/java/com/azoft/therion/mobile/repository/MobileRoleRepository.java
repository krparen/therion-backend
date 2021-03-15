package com.azoft.therion.mobile.repository;

import com.azoft.therion.mobile.entity.MobileRole;
import com.azoft.therion.mobile.entity.MobileRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MobileRoleRepository extends JpaRepository<MobileRole, Long> {
  Optional<MobileRole> findByName(MobileRoleName name);
}
