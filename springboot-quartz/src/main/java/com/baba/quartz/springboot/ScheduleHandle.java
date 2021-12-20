package com.baba.quartz.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Spring的定时器有三种模式,分别是fixedDelay、cron、fixedRate
 */
@Component
public class ScheduleHandle {
 
    private final Logger log = LoggerFactory.getLogger(ScheduleHandle.class);
 
    private List<Integer> index = Arrays.asList(8 * 1000, 3 * 1000, 6 * 1000, 2 * 1000, 2 * 1000);
 
    private AtomicInteger atomicInteger = new AtomicInteger(0);


    /**
     * 上一个任务执行完成之后,间隔3秒(因为@Scheduled(fixedDelay = 3 * 1000))后,执行下一个任务
     */
//    @Scheduled(fixedDelay = 3 * 1000)
//    public void fixedDelay() throws Exception {
//        int i = atomicInteger.get();
//        if (i < 5) {
//            Integer sleepTime = index.get(i);
//            log.info("第{}个任务开始执行,执行时间为{}ms", i, sleepTime);
//            Thread.sleep(sleepTime);
//            atomicInteger.getAndIncrement();
//        }
//    }


    /**
     * 你可以理解为5s就是一个周期.这就相当于在宿舍洗澡,因为只有一个洗澡位置(单线程),
     * 所以每次只能进去一个人,然后舍长在门口,每5s看一下有没有空位,有空位的话叫下一个进去洗澡.
     */
//    @Scheduled(cron = "0/5 * * * * ? ")
//    public void cron() throws Exception {
//        int i = atomicInteger.get();
//        if (i < 5) {
//            Integer sleepTime = index.get(i);
//            log.info("第{}个任务开始执行,执行时间为{}ms", i, sleepTime);
//            Thread.sleep(sleepTime);
//            atomicInteger.getAndIncrement();
//        }
//    }

    /**
     * 你可以理解为舍长预算每个同学洗澡的时间是5秒,但是第一个同学进去洗澡,用了8秒.
     *
     * 第二个同学本来应该在第5秒的时候就进去的,结果第一个同学出来的时候,已经是第8秒了,那么舍长就赶紧催第二个同学进去,把时间进度追回来.
     *
     * 第二个同学知道时间紧,洗了3秒就出来.此时舍长发现,第三个同学,原本应该是在第10秒进去的,结果现在已经到了第11秒(8+3),那么就赶紧催第三个同学进去.
     *
     * 第三个同学沉醉其中,难以自拔的洗了6秒.出来的时候已经是第17秒(8+3+6).舍长掐指一算,发现第四个同学原本应该是第15秒的时候就进去了.结果现在都17秒了,时间不等人,催促第四个同学进去赶紧洗.
     *
     * 第四个同学只洗了2秒就出来了,出来的时候,舍长看了一下时间,是第19秒."有原则"的舍长发现,第5个同学原本预算是20秒的时候进去的,结果现在才19秒,不行,那让他在外面玩1秒的手机,等20秒的时候再进去.
     */
//    @Scheduled(fixedRate = 5 * 1000)
//    public void fixedRate() throws Exception {
//        int i = atomicInteger.get();
//        if (i < 5) {
//            Integer sleepTime = index.get(i);
//            log.info("第{}个任务开始执行,执行时间为{}ms", i, sleepTime);
//            Thread.sleep(sleepTime);
//            atomicInteger.getAndIncrement();
//        }
//    }

}