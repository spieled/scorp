package cn.studease.util.log;

import com.jcabi.log.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Author: liushaoping
 * Date: 2015/7/21.
 */
@Aspect
public class MethodLogger {

    @Around("execution(* *(..)) && @annotation(Loggable)")
    public Object around(ProceedingJoinPoint point) {
        System.out.println("logger aspect in process");
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Logger.info("#%s(%s): %s in %[msec]s", MethodSignature.class.cast(point.getSignature()).getMethod().getName(), point.getArgs(), result, System.currentTimeMillis() - start);
        return result;

    }

}
