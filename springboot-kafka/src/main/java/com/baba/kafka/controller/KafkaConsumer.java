package com.baba.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.joda.time.DateTime;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
public class KafkaConsumer {
    @KafkaListener(topics = KafkaProducer.TOPIC_TEST,groupId = KafkaProducer.TOPIC_GROUP1)
    public void consumerGroup1(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        DateTime time= new DateTime();
        String dateTime = time.toString("yyyy-MM-dd hh:mm:ss");
        log.info("group1接收到消息时间：{}",dateTime);
        Optional message = Optional.ofNullable(record.value());
        if(message.isPresent()){
            Object msg = message.get();
            log.info("consumerGroup1 消费了： Topic:" + topic + ",Message:" + msg);
            //手动提交偏移量
            ack.acknowledge();
        }
    }

    @KafkaListener(topics = KafkaProducer.TOPIC_TEST,groupId = KafkaProducer.TOPIC_GROUP2)
    public void consumerGroup2(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        DateTime time= new DateTime();
        String dateTime = time.toString("yyyy-MM-dd hh:mm:ss");
        log.info("group2接收到消息时间：{}",dateTime);
        Optional message = Optional.ofNullable(record.value());
        if(message.isPresent()){
            Object msg = message.get();
            log.info("consumerGroup2 消费了：Topic:" + topic + ",Message:" + msg);
            //手动提交偏移量
            ack.acknowledge();
        }
    }
}
