package com.github.mgljava.wxsignature.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Cache {

  //键值对集合
  private final static Map<String, Entity> map = new HashMap<>();

  //定时器线程池，用于清除过期缓存
  private final static ScheduledExecutorService executor = Executors
      .newSingleThreadScheduledExecutor();

  public synchronized static void put(String key, Object data) {
    Cache.put(key, data, 0);
  }

  public synchronized static void put(String key, Object data, long expire) {
    //清除原键值对
    Cache.remove(key);
    //设置过期时间
    if (expire > 0) {
      Future future = executor.schedule(() -> {
        //过期后清除该键值对
        synchronized (Cache.class) {
          map.remove(key);
        }
      }, expire, TimeUnit.MILLISECONDS);
      map.put(key, new Entity(data, future));
    } else {
      //不设置过期时间
      map.put(key, new Entity(data, null));
    }
  }

  public synchronized static Object get(String key) {
    Entity entity = map.get(key);
    return entity == null ? null : entity.getValue();
  }

  public synchronized static <T> T get(String key, Class<T> clazz) {
    return clazz.cast(Cache.get(key));
  }

  public synchronized static Object remove(String key) {
    //清除原缓存数据
    Entity entity = map.remove(key);
    if (entity == null) {
      return null;
    }
    //清除原键值对定时器
    Future future = entity.getFuture();
    if (future != null) {
      future.cancel(true);
    }
    return entity.getValue();
  }

  public synchronized static int size() {
    return map.size();
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  private static class Entity {

    //键值对的value
    private Object value;
    //定时器Future
    private Future future;
  }
}


