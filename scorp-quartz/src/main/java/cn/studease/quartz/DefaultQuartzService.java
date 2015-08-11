package cn.studease.quartz;

import cn.studease.annotation.J;
import cn.studease.util.Dir;
import cn.studease.util.ReflectUtil;
import cn.studease.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.*;


/**
 * Author: liushaoping
 * Date: 2015/8/11.
 */
public class DefaultQuartzService implements QuartzService {

    private static final String NAME = "name";
    private static final String JOB_NAME = "jobName";
    private static final String JOB_CLASS_NAME = "jobClassName";
    private static final String TRIGGER_STATE = "triggerState";
    private static final String NEXT_FIRE_TIME = "nextFireTime";
    private String[] packagesToScan = {"cn.studease.quartz.job"};
    private Scheduler scheduler;
    private Map<String, String> jobNameMap = new HashMap();

    public String getJobName(String jobClassName) {
        String jobName = (String) this.jobNameMap.get(jobClassName);
        if (jobName == null) {
            try {
                jobName = ((J) Class.forName(jobClassName).getAnnotation(J.class)).value();
            } catch (Exception e) {
                jobName = jobClassName;
            }
            this.jobNameMap.put(jobClassName, jobName);
        }
        return jobName;
    }


    public List<JSONObject> getTriggers(String sort, Dir dir) {
        if (!StringUtil.hasText(sort)) {
            sort = "nextFireTime";
        }
        if (dir == null) {
            dir = Dir.ASC;
        }
        List<JSONObject> list = new ArrayList();
        try {
            for (String triggerName : this.scheduler.getTriggerNames("DEFAULT")) {
                CronTrigger trigger = (CronTrigger) this.scheduler.getTrigger(triggerName, "DEFAULT");
                JSONObject json = JSON.parseObject(JSON.toJSONString(trigger));
                json.put("triggerState", Integer.valueOf(this.scheduler.getTriggerState(trigger.getName(), "DEFAULT")));
                json.put("jobName", getJobName(trigger.getName()));
                list.add(json);
            }
        } catch (Exception ignored) {
        }
        //Util.sort(list, sort, dir);
        return list;
    }


    public String addTrigger(String jobClassName, String cronExpression) {
        try {
            removeTrigger(jobClassName);
            JobDetail jobDetail = new JobDetail(jobClassName, Class.forName(jobClassName));
            this.scheduler.addJob(jobDetail, true);
            CronTrigger cronTrigger = new CronTrigger(jobClassName, "DEFAULT", jobDetail.getName(), "DEFAULT");
            cronTrigger.setCronExpression(new CronExpression(cronExpression));
            this.scheduler.scheduleJob(cronTrigger);
            this.scheduler.rescheduleJob(cronTrigger.getName(), cronTrigger.getGroup(), cronTrigger);
        } catch (Exception e) {
            return "添加失败：" + e.getMessage();
        }
        return null;
    }


    public String pauseTrigger(String jobClassName) {
        try {
            this.scheduler.pauseTrigger(jobClassName, "DEFAULT");
        } catch (Exception e) {
            return "暂停失败：" + e.getMessage();
        }
        return null;
    }


    public String pauseAllTriggers() {
        try {
            this.scheduler.pauseAll();
        } catch (SchedulerException e) {
            return "暂停失败：" + e.getMessage();
        }
        return null;
    }


    public String resumeTrigger(String jobClassName) {
        try {
            this.scheduler.resumeJob(jobClassName, "DEFAULT");
        } catch (SchedulerException e) {
            return "恢复失败：" + e.getMessage();
        }
        return null;
    }


    public String resumeAllTriggers() {
        try {
            this.scheduler.resumeAll();
        } catch (SchedulerException e) {
            return "恢复失败：" + e.getMessage();
        }
        return null;
    }


    public String removeTrigger(String jobClassName) {
        try {
            pauseTrigger(jobClassName);
            this.scheduler.unscheduleJob(jobClassName, "DEFAULT");
        } catch (Exception e) {
            return "删除失败：" + e.getMessage();
        }
        return null;
    }


    public List<Map<String, Object>> getUnScheduledJobs() {
        List<Class<?>> allJobs = new ArrayList();
        for (String p : this.packagesToScan) {
            allJobs.addAll(ReflectUtil.getClasses(p));
        }


        for (JSONObject json : getTriggers(null, null)) {
            try {
                allJobs.remove(Class.forName(json.getString("name")));
            } catch (Exception ignored) {
            }
        }


        List<Map<String, Object>> result = new ArrayList();

        for (Class<?> job : allJobs) {

            boolean hasJob = false;
            boolean hasSerializable = false;
            for (Class<?> clazz : job.getInterfaces()) {
                if (clazz.equals(Job.class)) {
                    hasJob = true;
                }
                if (clazz.equals(Serializable.class)) {
                    hasSerializable = true;
                }
            }
            if ((hasJob) && (hasSerializable)) {


                String triggerName = job.getName();
                String jobName = triggerName;
                try {
                    jobName = ((J) job.getAnnotation(J.class)).value();
                } catch (Exception ignored) {
                }
                Map<String, Object> map = new HashMap();
                map.put("jobName", jobName);
                map.put("jobClassName", triggerName);
                result.add(map);
            }
        }
        return result;
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public String[] getPackagesToScan() {
        return this.packagesToScan;
    }

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
}