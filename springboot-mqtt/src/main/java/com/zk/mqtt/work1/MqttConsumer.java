package com.zk.mqtt.work1;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MqttConsumer implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(MqttConsumer.class);

    // 默认的消息推送主题，实际可在调用接口时指定
    private static String MQTT_TOPIC = "topic";
    // 服务器连接地址，如果有多个，用逗号隔开
    private static String MQTT_HOST = "tcp://114.215.138.22:1883";
    //private static String MQTT_HOST = "tcp://127.0.0.1:1883";
    // 连接服务器默认客户端ID
    private static String MQTT_CLIENT_ID = "client_id_zk";
    // 用户名
    private static String MQTT_USER_NAME = "";
    // 密码
    private static String MQTT_PASSWORD = "";
    // 连接超时
    private static Integer MQTT_TIMEOUT = 60;
    private static Integer MQTT_KEEP_ALIVE = 30;

    private static MqttClient client;

    @Override
    public void run(ApplicationArguments args) {
        log.info("初始化并启动mqtt......");
        this.connect();
    }

    /**
     * 连接mqtt服务器
     */
    private void connect() {
        try {
            // 创建客户端
            this.getClient();
            // 设置配置
            MqttConnectOptions options = this.getOptions();
            String[] topic = MQTT_TOPIC.split(",");
            // 消息发布质量
            int[] qos = this.getQos(topic.length);
            // 最后设置
            this.create(options, topic, qos);
        } catch (Exception e) {
            log.error("mqtt连接异常：" + e);
        }
    }

    /**
     * 创建客户端  
     */
    public void getClient() {
        try {
            if (null == client) {
                // MemoryPersistence设置clientid的保存形式，默认为以内存保存
                //client = new MqttClient(PropertiesUtil.MQTT_HOST, RandomUtil.uuid(), new MemoryPersistence());
                client = new MqttClient(MQTT_HOST, MQTT_CLIENT_ID, new MemoryPersistence());
            }
            log.info("创建mqtt客户端");
        } catch (Exception e) {
            log.error("创建mqtt客户端异常：" + e);
        }
    }

    /**
     * 生成配置对象，用户名，密码等 
     */
    public MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        //options.setUserName(MQTT_USER_NAME);
        //options.setPassword(MQTT_PASSWORD.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(MQTT_TIMEOUT);
        // 设置会话心跳时间
        options.setKeepAliveInterval(MQTT_KEEP_ALIVE);
        // 是否清除session（主要：本项目设置成false（session不会删除），重启项目订阅会报错，原因是重启项目会重新创建新的session，与老的session冲突）
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        log.info("--生成mqtt配置对象");
        return options;
    }

    /**
     * qos消息发布质量
     */
    public int[] getQos(int length) {

        int[] qos = new int[length];
        for (int i = 0; i < length; i++) {
            /**
             *  MQTT协议中有三种消息发布服务质量:
             *
             * QOS0： “至多一次”，消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这一级别可用于如下情况，环境传感器数据，丢失一次读记录无所谓，因为不久后还会有第二次发送。
             * QOS1： “至少一次”，确保消息到达，但消息重复可能会发生。
             * QOS2： “只有一次”，确保消息到达一次。这一级别可用于如下情况，在计费系统中，消息重复或丢失会导致不正确的结果，资源开销大
             */
            qos[i] = 0;
        }
        log.info("--设置消息发布质量");
        return qos;
    }

    /**
     * 装载所需实例和订阅主题
     */
    public void create(MqttConnectOptions options, String[] topic, int[] qos) {
        try {
            client.setCallback(new MqttConsumerCallback(client, options, topic, qos));
            log.info("--添加回调处理类");
            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("装载实例异常：" + e);
        }
    }

    /**
     * 订阅某个主题*/
    public static void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
            log.info("订阅 topic：" + topic + " qos：" + qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布，
     * retained = false 非持久化
     * <p>
     * qos根据文档设置*/
    public static void publish(String topic, String msg, int qos) {
        if (null != client && client.isConnected()) {
               publish(qos, false, topic, msg);
        }
    }

    /**
     * 发布
     */
    public static void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        if (StringUtils.hasText(pushMessage)) {
            message.setPayload(pushMessage.getBytes());
        }
        MqttTopic mTopic = client.getTopic(topic);
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
            if (token.isComplete()) {
                log.info("消息发布成功，topic：{} msg：{}", topic, pushMessage);
            } else {
                log.info("消息发布失败，topic：" + topic);
            }
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}