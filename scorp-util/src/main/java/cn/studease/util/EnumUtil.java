package cn.studease.util;

import org.slf4j.LoggerFactory;

/**
 * 枚举工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class EnumUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EnumUtil.class);

    public static <T extends Enum<T>> T parseEnumByName(Class<T> clazz, String value) {
        try {
            return Enum.valueOf(clazz, value);
        } catch (Exception e) {
            log.trace("解析枚举类型失败", e);
        }
        return null;
    }


    public static <T extends Enum<T>> BaseEnum parseEnumByDisplay(Class<T> clazz, String display) {
        try {
            for (T t : java.util.EnumSet.allOf(clazz)) {
                if (display.equals(((BaseEnum) t).getDisplay())) {
                    return (BaseEnum) t;
                }
            }
        } catch (Exception e) {
            log.trace("从显示名称解析成枚举类型失败", e);
        }
        return null;
    }
}
