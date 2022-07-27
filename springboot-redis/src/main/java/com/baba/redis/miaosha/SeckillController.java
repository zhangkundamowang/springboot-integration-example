package com.baba.redis.miaosha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private SeckillByScript seckillByScript;
    /**
     * ab -n 1000 -c 100 -p ~/postfile -T application/x-www-form-urlencoded  http://192.168.56.1:8090/skill
     * ab工具模拟并发
     * -n  最大的请求数
     * -c  同时最大的并发数
     * -p  postfile  指定post请求的参数
     * -T 指定请求的content-type
     * 可以通过vim postfile 模拟表单提交参数，以&符号结尾，存放在当前目录，输入内容 prodid=1010& （PS: 在本次测试中实际上没有到）
     */

    @RequestMapping(value = "/skill",method = RequestMethod.POST)
    public boolean setandGetValue() {
        String prodid = "1010";
        StringBuilder uidSB = new StringBuilder("");
        for (int i = 0; i < 6; i++) {
            int nextInt = new Random().nextInt(10);
            uidSB.append(nextInt);
        }
        //boolean result = seckillService.doSeckill(uidSB.toString(), prodid);
        boolean result = seckillByScript.doSecKill(uidSB.toString(), prodid);
        return result;
    }

}
