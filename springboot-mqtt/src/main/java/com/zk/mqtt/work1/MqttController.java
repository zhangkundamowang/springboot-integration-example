package com.zk.mqtt.work1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttController {

    @RequestMapping(value = "/mqtt")
    public void mqtt(){
      MqttUtil.push("topic","测试发送消息成功",1);
    }

}
