package com.azoft.therion.mobile.repository;

import com.azoft.therion.mobile.entity.MobileToken;
import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileTokenRepository extends JpaRepository<MobileToken, Long> {

  MobileToken findFirstByMobileUserIdAndTokenAndExpireAtIsAfter(
      Long mobileUserId, String token, OffsetDateTime expireAt);
}
