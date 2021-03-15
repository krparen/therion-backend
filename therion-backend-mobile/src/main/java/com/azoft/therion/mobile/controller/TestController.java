package com.azoft.therion.mobile.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

  @RequestMapping({ "/hello" })
  public String firstPage() {
    return "Hello World";
  }

}
