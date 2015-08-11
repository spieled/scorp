package cn.studease.quartz.job;

import java.io.Serializable;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Author: liushaoping
 * Date: 2015/8/11.
 */
public class SampleJob implements Job, Serializable {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("示例任务，上下文：" + context);
    }
}
