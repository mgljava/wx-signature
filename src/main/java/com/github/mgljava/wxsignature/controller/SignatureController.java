package com.github.mgljava.wxsignature.controller;

import com.github.mgljava.wxsignature.cache.CacheManager;
import com.github.mgljava.wxsignature.service.EncryptService;
import com.github.mgljava.wxsignature.utils.HttpClientUtils;
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

  private final EncryptService encryptService;

  /**
   * @param noncestr 随机字符串
   * @param timestamp 时间戳
   * @param url url地址
   */
  @GetMapping(value = "/get")
  public ResponseEntity getSignature(@RequestParam("noncestr") String noncestr,
      @RequestParam("timestamp") String timestamp,
      @RequestParam("url") String url) {
    String accessToken = CacheManager.get("token");
    String wxChatTicket = CacheManager.get("ticket");
    if (accessToken == null) {
      System.out.println("accessToken == null");
      accessToken = HttpClientUtils.getWxChatAccessToken();
      CacheManager.put("token", accessToken, 7200);
    }
    if (wxChatTicket == null) {
      System.out.println("wxChatTicket == null");
      wxChatTicket = HttpClientUtils.getWxChatTicket(accessToken);
      CacheManager.put("ticket", wxChatTicket, 7200);
    }
    String code =
        "jsapi_ticket=" + wxChatTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp
            + "&url=" + url;
    System.out.println("accessToken : " + accessToken);
    System.out.println("wxChatTicket : " + wxChatTicket);
    return ResponseEntity.ok(encryptService.encode(code));
  }
}
