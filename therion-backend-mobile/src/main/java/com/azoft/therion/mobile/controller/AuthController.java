package com.azoft.therion.mobile.controller;

import com.azoft.therion.mobile.converter.Converter;
import com.azoft.therion.mobile.dto.JwtAuthenticationResponse;
import com.azoft.therion.mobile.dto.LoginRequest;
import com.azoft.therion.mobile.dto.MobileUserDto;
import com.azoft.therion.mobile.dto.RequestCodeDto;
import com.azoft.therion.mobile.dto.SignUpRequest;
import com.azoft.therion.mobile.entity.MobileRole;
import com.azoft.therion.mobile.entity.MobileRoleName;
import com.azoft.therion.mobile.entity.MobileToken;
import com.azoft.therion.mobile.entity.MobileUser;
import com.azoft.therion.mobile.exception.AppException;
import com.azoft.therion.mobile.exception.ErrorCode;
import com.azoft.therion.mobile.exception.ErrorCodeException;
import com.azoft.therion.mobile.security.JwtTokenProvider;
import com.azoft.therion.mobile.security.UserPrincipal;
import com.azoft.therion.mobile.service.MobileRoleService;
import com.azoft.therion.mobile.service.MobileTokenService;
import com.azoft.therion.mobile.service.MobileUserService;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

  @Value("${application.jwt.token-ttl-in-minutes}")
  private int tokenTtlInMinutes;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private MobileUserService mobileUserService;

  @Autowired
  private MobileRoleService mobileRoleService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private MobileTokenService mobileTokenService;

  @Autowired
  private Converter<MobileUser, MobileUserDto> userToDtoConverter;

  @PostMapping("/code")
  public void requestCode(@RequestBody @Valid RequestCodeDto request) {
    mobileUserService.sendAuthorizationCode(request.getPhone());
  }

  @PostMapping("/signin")
  public JwtAuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getPhone(),
            loginRequest.getPassword()
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    final Long id = ((UserPrincipal) authentication.getPrincipal()).getId();
    final MobileUser user = Optional.ofNullable(mobileUserService.getOne(id)).orElseThrow(() -> {
      String message = "No user for id " + id;
      log.error(message);
      return new IllegalArgumentException(message);
    });

    String jwt = tokenProvider.generateToken(authentication);

    MobileToken tokenEntity = new MobileToken();
    tokenEntity.setMobileUserId(id);
    tokenEntity.setToken(jwt);
    tokenEntity.setExpireAt(OffsetDateTime.now().plusMinutes(tokenTtlInMinutes));
    mobileTokenService.save(tokenEntity);

    return new JwtAuthenticationResponse(jwt, userToDtoConverter.convert(user));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

    if (mobileUserService.existsByPhone(signUpRequest.getPhone())) {
      String message = "phone " + signUpRequest.getPhone() + " is taken";
      log.error(message);
      throw new ErrorCodeException(message, ErrorCode.PHONE_EXISTS);
    }

    // Creating user's account
    MobileUser mobileUser = new MobileUser(signUpRequest.getName(), signUpRequest.getPhone(), signUpRequest.getPassword());

    mobileUser.setPassword(passwordEncoder.encode(mobileUser.getPassword()));

    MobileRole userRole = mobileRoleService.findByName(MobileRoleName.ROLE_MOBILE_USER)
        .orElseThrow(() -> new AppException("User Role not set."));

    mobileUser.setMobileRoles(Collections.singleton(userRole));

    MobileUser result = mobileUserService.save(mobileUser);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
