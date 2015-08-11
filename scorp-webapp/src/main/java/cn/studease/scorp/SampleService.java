package cn.studease.scorp;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Author: liushaoping
 * Date: 2015/8/5.
 */
@Service
public class SampleService implements InitializingBean {

    private void foo() {
        try {
            Thread.sleep(1000);
            System.out.println(" foo is running");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void bar() {
        try {
            Thread.sleep(1000);
            System.out.println(" bar is running");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Thread currentThread = new Thread() {
            public void run() {
                foo();
            }
        };
        currentThread.setDaemon(false);
        currentThread.start();

        Thread t = new Thread() {
            public void run() {
                bar();
            }
        };
        t.setDaemon(true);
        t.start();

    }
}
