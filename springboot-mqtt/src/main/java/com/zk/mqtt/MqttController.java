package com.zk.mqtt;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class MqttController {

    public void mqtt(){
      MqttUtil.push("topic","测试发送消息成功",1);
    }

}
