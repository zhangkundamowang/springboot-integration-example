package com.baba.kafka.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "kafka操作类",description = "kafka操作类")
public class KafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 自定义topic
     */
    public static final String TOPIC_TEST = "topic.test";

    /**
     * 组别1
     */
    public static final String TOPIC_GROUP1 = "topic.group1";

    /**
     * 组别2
     */
    public static final String TOPIC_GROUP2 = "topic.group2";

    @PostMapping(value = "/pushMessage")
    @ApiOperation(notes = "kafka推送消息",value = "kafka推送消息")
    public void pushMessage(String message){
        log.info("准备发送消息信息：{}",message);
        //发送消息
        ListenableFuture<SendResult<String, Object>> send = kafkaTemplate.send(TOPIC_TEST, message);
        DateTime time= new DateTime();
        String dateTime = time.toString("yyyy-MM-dd hh:mm:ss");
        log.info("topic：{}发送消息完成,完成时间：{}",TOPIC_TEST,dateTime);
        send.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error(TOPIC_TEST+"-生产者发送消息失败"+throwable.getMessage());
            }

            @Override
            public void onSuccess(@Nullable SendResult<String, Object> stringObjectSendResult) {
                DateTime time= new DateTime();
                String dateTime = time.toString("yyyy-MM-dd hh:mm:ss");
                log.info(TOPIC_TEST+"-生产者发送消息成功"+stringObjectSendResult.toString()+"时间："+dateTime);
            }
        });
    }
}
