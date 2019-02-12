package com.github.mgljava.wxsignature.utils;

import static com.github.mgljava.wxsignature.constant.WxConstant.GET_ACCESS_TOKEN_URL;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

  public static String getWxChatAccessToken() {
    String result = "";
    HttpGet request = new HttpGet(GET_ACCESS_TOKEN_URL);
    HttpClient httpClient = HttpClientBuilder.create().build();
    result = getResponse(result, request, httpClient);
    return JsonPath.parse(result).read("$.access_token");
  }

  public static String getWxChatTicket(String accessToken) {
    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
    String result = "";
    HttpGet request = new HttpGet(url);
    HttpClient httpClient = HttpClientBuilder.create().build();
    result = getResponse(result, request, httpClient);
    return JsonPath.parse(result).read("$.ticket");
  }

  private static String getResponse(String result, HttpGet request, HttpClient httpClient) {
    try {
      HttpResponse response = httpClient.execute(request);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        result = EntityUtils.toString(response.getEntity(), "utf-8");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}
