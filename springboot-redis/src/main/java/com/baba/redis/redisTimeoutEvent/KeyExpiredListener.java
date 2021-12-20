package com.baba.redis.redisTimeoutEvent;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

/**
 * 监听key过期
 */
@Slf4j
@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        //获取失效key名称
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("expireKey---" + key);
        ThreadUtil.execAsync(() -> {
            handle(key);
        });

        //获取key原本的value 获取不到 是null
        String expireKeyValue = redisTemplate.opsForValue().get(key);
        log.info("expireKeyValue---" + expireKeyValue);
    }

    public void handle(String key) {
        //如果一部分key过期后执行的逻辑相同 给这些key加一个统一的前缀 比如in_bat
        if (key.startsWith("in_bat")) {
            log.info("正在执行具有统一前缀in_bat的key的过期逻辑");
        } else if (key.startsWith("张")) {
            log.info("正在执行具有统一前缀张的key的过期逻辑");
        } else {
            log.info("正在执行key为" + key + "过期逻辑");
        }
    }

}
