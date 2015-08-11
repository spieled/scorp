package cn.studease.quartz;

import cn.studease.util.Dir;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import org.quartz.Scheduler;

/**
 * Author: liushaoping
 * Date: 2015/8/11.
 */
public interface QuartzService {

    public abstract String getJobName(String paramString);

    public abstract List<JSONObject> getTriggers(String paramString, Dir paramDir);

    public abstract String addTrigger(String paramString1, String paramString2);

    public abstract String pauseTrigger(String paramString);

    public abstract String pauseAllTriggers();

    public abstract String resumeTrigger(String paramString);

    public abstract String resumeAllTriggers();

    public abstract String removeTrigger(String paramString);

    public abstract List<Map<String, Object>> getUnScheduledJobs();

    public abstract Scheduler getScheduler();

    public abstract void setScheduler(Scheduler paramScheduler);

    public abstract String[] getPackagesToScan();

    public abstract void setPackagesToScan(String[] paramArrayOfString);
}