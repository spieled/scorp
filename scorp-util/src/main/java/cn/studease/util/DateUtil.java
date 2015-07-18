package cn.studease.util;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.LoggerFactory;

/**
 * 日期相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class DateUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static Date parseDate(String str, String format, Date def) {
        try {
            return DateUtils.parseDate(str, new String[]{format});
        } catch (Exception e) {
        }
        return def;
    }


    public static Date parseDate(int someYmd) {
        return parseDate(someYmd + "", "yyyyMMdd", null);
    }

    public static int parseYmd() {
        return parseYmd(System.currentTimeMillis());
    }

    public static int parseYmd(Date date) {
        return parseYmd(date.getTime());
    }


    public static int parseYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(1);
    }


    public static int parseYear(int someYmd) {
        return someYmd / 10000;
    }


    public static int parseYear() {
        return parseYear(System.currentTimeMillis());
    }


    public static int parseYmd(long time) {
        return Integer.parseInt(DateFormatUtils.format(time, "yyyyMMdd"));
    }

    public static int parseYm() {
        return parseYm(System.currentTimeMillis());
    }


    public static int parseYm(long time) {
        return Integer.parseInt(DateFormatUtils.format(time, "yyyyMM"));
    }

    public static int parseYm(int someYmd) {
        return someYmd / 100;
    }

    public static List<Integer> parseYmsBetween(int startYmd, int endYmd) {
        List<Integer> set = new ArrayList();
        for (Iterator i$ = parseYmdsBetween(startYmd, endYmd).iterator(); i$.hasNext(); ) {
            int someYmd = ((Integer) i$.next()).intValue();
            int someYm = parseYm(someYmd);
            if (!set.contains(Integer.valueOf(someYm))) {
                set.add(Integer.valueOf(someYm));
            }
        }
        return set;
    }

    public static List<Integer> parseYmsBetween(String startDate, String endDate) {
        return parseYmsBetween(parseYmd(startDate), parseYmd(endDate));
    }

    public static List<Integer> parseYmsBetween(Date startDate, Date endDate) {
        return parseYmsBetween(parseYmd(startDate.getTime()), parseYmd(endDate.getTime()));
    }

    public static int parseYmd(String date) {
        return parseInt(date.replaceAll("-", ""), 0);
    }

    public static long parseStartTimestamp(int someYmd) {
        return parseStartDate(someYmd).getTime();
    }

    public static Date parseStartDate(int someYmd) {
        try {
            return DateUtils.parseDate(someYmd + "", new String[]{"yyyyMMdd"});
        } catch (Exception e) {
            throw new RuntimeException("无法解析成日期：" + someYmd);
        }
    }

    public static long parseEndTimestamp(int someYmd) {
        return parseEndDate(someYmd).getTime();
    }

    public static Date parseEndDate(int someYmd) {
        try {
            return DateUtils.parseDate(someYmd + "235959999", new String[]{"yyyyMMddHHmmssSSS"});
        } catch (Exception e) {
            throw new RuntimeException("无法解析成日期：" + someYmd);
        }
    }

    public static List<Integer> parseYmdsBetween(int startYmd, int endYmd) {
        ArrayList<Integer> dates = new ArrayList();

        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(parseStartTimestamp(startYmd));
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(parseStartTimestamp(endYmd));

        if (c1.compareTo(c2) > 0) {
            return dates;
        }
        for (; ; ) {
            dates.add(Integer.valueOf(parseYmd(c1.getTimeInMillis())));
            c1.add(5, 1);
            if (c1.compareTo(c2) > 0) {
                break;
            }
        }
        return dates;
    }


    public static int parseYmd(HttpServletRequest request, String name) {
        try {
            return Integer.parseInt(request.getParameter(name).replaceAll("-", ""));
        } catch (Exception e) {
            log.trace("从HttpServletRequest取YMD对象失败", e);
        }
        return 0;
    }


    public static int parseStartYmd(HttpServletRequest request) {
        try {
            return parseInt(request.getParameter("startYmd").replaceAll("-", ""), 0);
        } catch (Exception e) {
            log.trace("从HttpServletRequest取YMD对象失败", e);
        }
        return 0;
    }


    public static int parseEndYmd(HttpServletRequest request) {
        try {
            return parseInt(request.getParameter("endYmd").replaceAll("-", ""), 0);
        } catch (Exception e) {
            log.trace("从HttpServletRequest取YMD对象失败", e);
        }
        return 0;
    }


    public static String formatDate(long time) {
        return formatDate(new Date(time));
    }


    public static String formatDate(int someYmd) {
        return formatDate(parseDate(someYmd));
    }


    public static String formatDate(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }


    public static String formatDateTime(long time) {
        return DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
    }


    public static String formatDateTime(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static int parseInt(String str, int def) {
        try {
            if ((str != null && str.trim().length() > 0) && (str.indexOf("-") > 0)) {
                str = str.replaceAll("-", "");
            }
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return def;
    }


}
