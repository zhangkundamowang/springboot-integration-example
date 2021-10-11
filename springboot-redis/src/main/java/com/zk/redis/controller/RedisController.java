package com.zk.redis.controller;

import com.zk.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class RedisController {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;


    /**
     * 测试key过期事件
     */
    @RequestMapping(value = "/redisTest", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void redisTest() {
        //原始方法  利用redisTemplate对象操作
        // redisTemplate.opsForValue().set("myKey", "myValue", 5, TimeUnit.SECONDS);
        // String myKey = redisTemplate.opsForValue().get("myKey");

        //redis中新增10秒后过期的key  用于测试监听key过期
        RedisUtil.setTime("myKey", "myValue", 10);
        RedisUtil.setTime("张大胆", "zdd", 10);

        //redis的java客户机端jedis基本使用
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", "张三");
        map.put("nickname", "江南");
        RedisUtil.getJedis().hmset("用户", map);

    }
}
