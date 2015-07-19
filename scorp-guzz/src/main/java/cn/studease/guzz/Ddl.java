package cn.studease.guzz;

import cn.studease.util.ReflectUtil;
import cn.studease.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import org.guzz.annotations.Entity;
import org.guzz.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class Ddl
        implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(Ddl.class);
    private boolean enabled;
    private boolean parallel;
    private int threads;
    private String[] packagesToScan;

    public Ddl() {
        this.enabled = true;

        this.parallel = false;

        this.threads = 1;
    }

    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            return;
        }
        if ((!this.enabled) || (!StringUtil.hasText(this.packagesToScan))) {
            return;
        }


        final List<Class<?>> classes = new ArrayList();
        for (String packageToScan : this.packagesToScan) {
            for (Class<?> clazz : ReflectUtil.getClasses(packageToScan)) {
                if ((clazz.getAnnotation(Entity.class) != null) && (clazz.getAnnotation(Table.class) != null)) {
                    classes.add(clazz);
                }
            }
        }


        // TODO classes 排序


        if (this.threads <= 1) {
            int index = 1;
            int classCount = classes.size();
            for (Class clazz : classes) {
                log.info("执行第 " + index++ + " 个实体的DDL，共有实体数：" + classCount);
                DdlUtil.checkEntity(clazz);
            }
            event.getApplicationContext().publishEvent(new DdlFinishEvent());
            return;
        }


        final Index entityIndex = new Index();
        final Index threadIndex = new Index();
        final int classCount = classes.size();

        for (int i = 0; i < this.threads; i++) {
            final int i1 = i + 1;
            new Thread(new Runnable() {
                public void run() {
                    int index;
                    do {
                        index = entityIndex.get();
                        if (index < classCount) {
                            Ddl.log.info("线程号：" + i1 + "，执行第 " + (index + 1) + " 个实体的DDL，共有实体数：" + classCount);
                            Class clazz = (Class) classes.get(index);
                            DdlUtil.checkEntity(clazz);
                        }
                    } while (index < classCount);

                    int finishedThread = threadIndex.get() + 1;
                    int activeThread = Ddl.this.threads - finishedThread;
                    Ddl.log.info("线程号：" + i1 + "，当前线程的DDL操作完成，剩余活动线程数：" + activeThread);


                    if (activeThread == 0) {
                        event.getApplicationContext().publishEvent(new DdlFinishEvent());
                    }
                }
            }).start();
        }
    }

    public String[] getPackagesToScan() {
        return this.packagesToScan;
    }

    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getThreads() {
        return this.threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public boolean isParallel() {
        return this.parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
        DdlUtil.setParallel(parallel);
    }

    private class Index {
        private int index = -1;

        private Index() {
        }

        public synchronized int get() {
            this.index += 1;
            return this.index;
        }
    }
}

