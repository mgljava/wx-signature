package com.github.mgljava.wxsignature.controller;

import com.github.mgljava.wxsignature.service.SignatureService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/signature")
public class SignatureController {

  private final SignatureService signatureService;

  /**
   * @param noncestr 随机字符串
   * @param timestamp 时间戳
   * @param url url地址
   */
  @GetMapping(value = "/get")
  public ResponseEntity getSignature(@RequestParam("noncestr") String noncestr,
      @RequestParam("timestamp") String timestamp,
      @RequestParam("url") String url,
      HttpServletResponse response) {
    final String signature = signatureService.generateSignature(noncestr, timestamp, url);
    response.addHeader("Access-Control-Allow-Origin", "*");
    return ResponseEntity.ok(signature);
  }
}
