package com.zk.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * mqtt工具类
 */
public class MqttUtil {
    private static Logger log = LoggerFactory.getLogger(MqttUtil.class);
    /**
     * 往某一主题发送消息
     *
     * @param topic 主题
     * @param msg   内容
     * @param qos   *  MQTT协议中有三种消息发布服务质量:
     *              * QOS0： “至多一次”，消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这一级别可用于如下情况，环境传感器数据，丢失一次读记录无所谓，因为不久后还会有第二次发送。
     *              * QOS1： “至少一次”，确保消息到达，但消息重复可能会发生。
     *              * QOS2： “只有一次”，确保消息到达一次。这一级别可用于如下情况，在计费系统中，消息重复或丢失会导致不正确的结果，资源开销大
     */
    public static boolean push(String topic, String msg, int qos) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                MqttConsumer.publish(topic, msg, qos);
            }
        });
        return true;
    }

    /**
     * 订阅某个主题后就能接收这个主题的消息*/
    public static boolean subscribe(String topic, int qos) {
        MqttConsumer.subscribe(topic, qos);
        return true;
    }
}