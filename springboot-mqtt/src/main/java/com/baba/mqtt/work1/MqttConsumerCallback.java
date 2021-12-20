package com.baba.mqtt.work1;

import cn.hutool.core.thread.ThreadUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.util.Arrays;

/**
 * mqtt回调处理类
 */
public class MqttConsumerCallback implements MqttCallbackExtended {

    private static Logger log = LoggerFactory.getLogger(MqttConsumerCallback.class);

    private MqttClient client;
    private MqttConnectOptions options;
    private String[] topic;
    private int[] qos;

    public MqttConsumerCallback(MqttClient client, MqttConnectOptions options, String[] topic, int[] qos) {
        this.client = client;
        this.options = options;
        this.topic = topic;
        this.qos = qos;
    }

    /**
     * 断开重连
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.info("MQTT连接断开，发起重连......");
        try {
            if (null != client && !client.isConnected()) {
                log.error("尝试重新连接");
                client.reconnect();
            } else {
                log.info("尝试建立新连接");
                client.connect(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收到消息调用令牌中调用
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //log.info("deliveryComplete---------" + Arrays.toString(topic));
    }

    /**
     * 消息处理
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {

        try {
            ThreadUtil.execute(new Runnable() {
                @Override
                public void run() {
                    String msg = new String(message.getPayload());
                    if (StringUtils.hasText(msg))
                        log.info("topic：{}  msg：{}", topic, msg);
                }
            });
        } catch (Exception e) {
            log.info("处理mqtt消息异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * mqtt连接后订阅主题
     */
    @Override
    public void connectComplete(boolean b, String s) {
        try {
            if (null != topic && null != qos) {
                if (client.isConnected()) {
                    client.subscribe(topic, qos);
                    log.info("mqtt连接成功，客户端ID：" + client.getClientId());
                    log.info("--订阅主题：" + Arrays.toString(topic));
                } else {
                    log.info("mqtt连接失败，客户端ID：" + client.getClientId());
                }
            }
        } catch (Exception e) {
            log.info("mqtt订阅主题异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


}