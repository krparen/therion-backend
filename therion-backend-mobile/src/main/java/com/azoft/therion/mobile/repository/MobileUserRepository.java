package com.azoft.therion.mobile.repository;

import com.azoft.therion.mobile.entity.MobileUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MobileUserRepository extends JpaRepository<MobileUser, Long> {

  List<MobileUser> findByIdIn(List<Long> userIds);

  Optional<MobileUser> findByPhone(String phone);

  Boolean existsByPhone(String phone);
}
