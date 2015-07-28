package cn.studease.util.log;

import com.jcabi.aspects.Loggable;
import com.jcabi.log.Logger;

/**
 * Author: liushaoping
 * Date: 2015/7/21.
 */
public class Foo {

    public static void main(String[] args) {
        System.out.println(new Foo().power(2, 2));
    }

    @Loggable
    public double power(int x, int p) {
        Logger.info(this, "power(%d, %d) just called", x, p);
        return Math.pow(x, p);
    }

}
