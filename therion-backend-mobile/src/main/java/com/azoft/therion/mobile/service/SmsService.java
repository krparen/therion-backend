package com.azoft.therion.mobile.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

  public void sendSms(String phone, String text) {
    log.info("fake sending sms. Phone: {}, text: {}", phone, text);
  }
}
