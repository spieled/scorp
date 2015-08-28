package cn.studease.quartz.sampleapplication;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: liushaoping
 * Date: 2015/8/23.
 */
@DisallowConcurrentExecution
public class HelloJob implements Job {

    private static final String MESSAGE = "hello world";
    private static Logger logger = LoggerFactory.getLogger(HelloJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(MESSAGE);
    }
}
