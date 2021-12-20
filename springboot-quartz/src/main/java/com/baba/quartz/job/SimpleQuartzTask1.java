package com.baba.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleQuartzTask1 extends QuartzJobBean {
    /**
     * 实现QuartzJobBean的executeInternal()方法，即可定义工作内容
     */

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println("1-简单的单Quartz任务1开始---" + dateFormat.format(new Date()));
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            System.out.println("1-线程1阻塞失败。" + e);
        }
        System.out.println("1-简单的单Quartz任务1结束---" + dateFormat.format(new Date()));
    }
}
