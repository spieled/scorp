package cn.studease.quartz;

import cn.studease.util.Dir;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.Scheduler;


/**
 * Author: liushaoping
 * Date: 2015/8/11.
 */
public class DefaultQuartzService221 implements QuartzService {

    private static final String NAME = "name";
    private static final String JOB_NAME = "jobName";
    private static final String JOB_CLASS_NAME = "jobClassName";
    private static final String TRIGGER_STATE = "triggerState";
    private static final String NEXT_FIRE_TIME = "nextFireTime";
    private String[] packagesToScan = {"cn.studease.quartz.job"};
    private Scheduler scheduler;
    private Map<String, String> jobNameMap = new HashMap();

    // 


    @Override
    public String getJobName(String paramString) {
        return null;
    }

    @Override
    public List<JSONObject> getTriggers(String paramString, Dir paramDir) {
        return null;
    }

    @Override
    public String addTrigger(String paramString1, String paramString2) {
        return null;
    }

    @Override
    public String pauseTrigger(String paramString) {
        return null;
    }

    @Override
    public String pauseAllTriggers() {
        return null;
    }

    @Override
    public String resumeTrigger(String paramString) {
        return null;
    }

    @Override
    public String resumeAllTriggers() {
        return null;
    }

    @Override
    public String removeTrigger(String paramString) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getUnScheduledJobs() {
        return null;
    }

    @Override
    public Scheduler getScheduler() {
        return null;
    }

    @Override
    public void setScheduler(Scheduler paramScheduler) {

    }

    @Override
    public String[] getPackagesToScan() {
        return new String[0];
    }

    @Override
    public void setPackagesToScan(String[] paramArrayOfString) {

    }
}