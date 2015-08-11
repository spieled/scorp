package cn.studease.scorp.quartz.job;

import cn.studease.annotation.J;
import java.io.Serializable;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: liushaoping
 * Date: 2015/8/11.
 */
@J("日志Job")
public class LogJob implements Job, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(LogJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("LogJob is running ... ");
    }
}
