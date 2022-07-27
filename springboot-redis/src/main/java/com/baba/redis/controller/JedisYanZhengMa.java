package com.baba.redis.controller;

import redis.clients.jedis.Jedis;
import java.util.Random;

public class JedisYanZhengMa {

    public static void main(String[] args) {
        //模拟验证码发送
         //verifyCode("18056250974");
        //校验
         checkCode("18056250974","240436");


    }

    //验证码校验
    public static  void checkCode(String phone,String code){
        Jedis  jedis = new Jedis("192.168.56.3", 6379, 0);
        jedis.auth("root");
        String codeKey="verifyCode"+phone+":code";
        String redisCode = jedis.get(codeKey);
        if(redisCode!=null&&redisCode.equals(code)){
            System.out.println("成功");
        }else{
            System.out.println("失败");
        }
        jedis.close();
    }


    //每个手机一天最多只能发送三次， 模拟验证码存到redis中，设置过期时间
    public static void verifyCode(String phone){
        Jedis  jedis = new Jedis("192.168.56.3", 6379, 0);
        jedis.auth("root");
        //手机发送次数key
        String countKey="verifyCode"+phone+":count";
        //验证码key
        String codeKey="verifyCode"+phone+":code";

        //每个手机每天最多发送三次
        String count = jedis.get(countKey);
        if(count==null){
            jedis.setex(countKey,24*60*60,"1");
        }else if(Integer.valueOf(count)<3){
            jedis.incr(countKey);
        }else if(Integer.valueOf(count)>=3){
            System.out.println("此手机号已发送三次验证码！");
            jedis.close();
            return;//return 作用是判断验证码发送三次以后不更新redi里面的验证码
        }
        //验证码存放到redis
        jedis.setex(codeKey,120,generatorCode());
        jedis.close();
    }

    //生成6位验证码
    public static String generatorCode(){
       Random random=new Random();
       String code="";
        for (int i = 0; i < 6; i++) {
          int number=  random.nextInt(10);
          code+=number;
        }
       return code;
    }

}