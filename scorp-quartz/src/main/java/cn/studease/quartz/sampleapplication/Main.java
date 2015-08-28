package cn.studease.quartz.sampleapplication;

import java.util.Properties;
import org.quartz.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: liushaoping
 * Date: 2015/8/23.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        sampleQuartz();
    }

    private static void sampleQuartz() throws SchedulerException {
        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
        properties.put("org.quartz.scheduler.instanceName", "MyScheduler");
        properties.put("org.quartz.threadPool.threadCount", "3");
        properties.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        Scheduler scheduler = new StdSchedulerFactory(properties).getScheduler();
        JobDetail jobDetail = newJob(HelloJob.class).withIdentity("helloJob", "defaultGroup").build();

        Trigger trigger = newTrigger().withIdentity("helloTrigger", "defaultGroup")
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInMilliseconds(1000).repeatForever())
                .build();


        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        try {
            Thread.sleep(90L * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();

    }
}

