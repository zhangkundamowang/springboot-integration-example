package com.zk.quartz.config;

import com.zk.quartz.job.SimpleQuartzTask1;
import com.zk.quartz.job.SimpleQuartzTask2;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Scheduler – 核心调度器
 * Job – 任务
 * JobDetail – 任务描述
 * Trigger – 触发器（包括SimpleTrigger和CronTrigger）
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail simpleQuartzTaskDetail1(){
        return JobBuilder.newJob(SimpleQuartzTask1.class).withIdentity("simpleQuartzTask1").storeDurably().build();
    }

    @Bean
    public Trigger simpleQuartzTaskTrigger1(){
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");
        return TriggerBuilder.newTrigger().forJob(simpleQuartzTaskDetail1())
                .withIdentity("simpleQuartzTask1")
                .withSchedule(scheduleBuilder)
                .build();
    }


    // 使用jobDetail包装job
    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(SimpleQuartzTask2.class).withIdentity("simpleQuartzTask2").storeDurably().build();
    }

    // 把jobDetail注册到trigger上去
    @Bean
    public Trigger myJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(15).repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(myJobDetail())
                .withIdentity("simpleQuartzTask2")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
