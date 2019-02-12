package com.github.mgljava.wxsignature.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheManager {

  private final static ScheduledExecutorService executor = Executors
      .newSingleThreadScheduledExecutor();
  public static Map<String, String> map = new HashMap<>();

  public static String get(String key) {
    return map.get(key);
  }

  public static void put(String key, String value, int delay) {
    map.put(key, value);
    executor.schedule(() -> {
      synchronized (CacheManager.class) {
        map.remove(key);
      }
    }, delay, TimeUnit.SECONDS);
  }
}
