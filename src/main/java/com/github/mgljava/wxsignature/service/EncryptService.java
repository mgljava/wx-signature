package com.github.mgljava.wxsignature.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EncryptService {

  public String encode(String code) {
    if (StringUtils.isEmpty(code)) {
      return null;
    }
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
      messageDigest.update(code.getBytes());
      return getFormattedText(messageDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static final char[] HEX = {'0', '1', '2', '3', '4', '5',
      '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  private static String getFormattedText(byte[] bytes) {
    int len = bytes.length;
    StringBuilder buf = new StringBuilder(len * 2);
    for (byte aByte : bytes) {
      buf.append(HEX[(aByte >> 4) & 0x0f]);
      buf.append(HEX[aByte & 0x0f]);
    }
    return buf.toString();
  }
}
