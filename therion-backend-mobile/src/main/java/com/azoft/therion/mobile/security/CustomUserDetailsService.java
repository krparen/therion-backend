package com.azoft.therion.mobile.security;


import com.azoft.therion.mobile.entity.MobileUser;
import com.azoft.therion.mobile.repository.MobileUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private MobileUserRepository mobileUserRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String phone)
      throws UsernameNotFoundException {
    // Let people login with either username or email
    MobileUser mobileUser = mobileUserRepository.findByPhone(phone)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with username or email : " + phone)
        );

    return UserPrincipal.create(mobileUser);
  }

  // This method is used by JWTAuthenticationFilter
  @Transactional
  public UserDetails loadUserById(Long id) {
    MobileUser mobileUser = mobileUserRepository.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("User not found with id : " + id)
    );

    return UserPrincipal.create(mobileUser);
  }
}
