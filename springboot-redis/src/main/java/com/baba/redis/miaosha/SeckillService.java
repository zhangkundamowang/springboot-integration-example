package com.baba.redis.miaosha;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class SeckillService {

    //uid 是用户ID
    // prodid 是商品ID
    public boolean doSeckill(String uid, String prodid) {
        // 1.uid和prodid非空判断
        if (StringUtils.isAnyBlank(uid, prodid)) {
            System.out.println("uid和prodid为空");
            return false;
        }
        // 2.连接redis
        Jedis jedis = new Jedis("192.168.56.3", 6379);
        jedis.auth("root" );
        // 3.拼接key
        // 3.1. 库存key
        String skKey = "sk:" + prodid + ":qt";
        // 3.2. 秒杀成功用户key
        String userKey = "sk:" + prodid + ":user";
        // 4.获取库存，如果库存null，秒杀还没有开始
        String skValue = jedis.get(skKey);
        if (skValue == null) {
            System.out.println("秒杀还没开始，请等待");
            jedis.close();
            return false;
        }
        // 5.判断用户是否重复操作
        Boolean sismember = jedis.sismember(userKey, uid);
        if (sismember) {
            System.out.println("该用户已经抢过一次，请勿重复抢购");
            jedis.close();
            return false;
        }
        // 6. 判断如果商品数量，库存数量小于1，秒杀结束
        if (Integer.parseInt(skValue) <= 0) {
            System.out.println("该商品的库存不足，秒杀失败");
            jedis.close();
            return false;
        }
        // 7. 秒杀过程
        //    7.1. 库存-1
        jedis.decr(skKey);
        //    7.2. 用户加入到set集合中
        jedis.sadd(userKey, uid);
        System.out.println("秒杀成功");
        return true;
    }

}
