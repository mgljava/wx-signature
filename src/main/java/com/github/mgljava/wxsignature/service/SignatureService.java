package com.github.mgljava.wxsignature.service;

import com.github.mgljava.wxsignature.cache.CacheManager;
import com.github.mgljava.wxsignature.utils.HttpClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureService {

  private final EncryptService encryptService;

  public String generateSignature(String noncestr, String timestamp, String url) {
    String accessToken = CacheManager.get("token");
    String wxChatTicket = CacheManager.get("ticket");
    if (accessToken == null) {
      accessToken = HttpClientUtils.getWxChatAccessToken();
      CacheManager.put("token", accessToken, 7200);
    }
    if (wxChatTicket == null) {
      wxChatTicket = HttpClientUtils.getWxChatTicket(accessToken);
      CacheManager.put("ticket", wxChatTicket, 7200);
    }

    String code =
        "jsapi_ticket=" + wxChatTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp
            + "&url=" + url;
    log.info("accessToken : " + accessToken);
    log.info("wxChatTicket : " + wxChatTicket);
    return encryptService.encode(code);
  }
}
