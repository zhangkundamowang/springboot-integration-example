package com.zk.quartz.config;

import com.zk.quartz.job.QuartzJob1;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz有3个核心要素：调度器(Scheduler)、任务(Job)、触发器(Trigger)
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail1(){
        return JobBuilder.newJob(QuartzJob1.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger1(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1) //每一秒执行一次
                .repeatForever(); //永久重复，一直执行下去
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail1())
                .withSchedule(scheduleBuilder)
                .build();
    }

}