package com.github.mgljava.wxsignature.constant;

public class WxConstant {

  public static final String APP_ID = "wx6bb814ccc9aacdf3";
  public static final String APP_SECRET = "f10e42796b82aa520e1d45006892025e";

  public static final String GET_ACCESS_TOKEN_URL =
      "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APP_ID
          + "&secret=" + APP_SECRET;
  public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
      + "access_token=" + GET_ACCESS_TOKEN_URL
      + "&type=jsapi";
}
