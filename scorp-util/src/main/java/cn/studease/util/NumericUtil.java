package cn.studease.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数字相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class NumericUtil {

    public static int parseInt(String str, int def) {
        try {
            if ((str != null) && (str.indexOf("-") > 0)) {
                str = str.replaceAll("-", "");
            }
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return def;
    }

    public static BigDecimal parseBigDecimal(String str, BigDecimal def) {
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
        }
        return def;
    }


    public static long parseLong(String str, long def) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
        }
        return def;
    }


    public static float parseFloat(String str, float def) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
        }
        return def;
    }


    public static double parseDouble(String str, double def) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }
        return def;
    }


    public static boolean parseBoolean(String str, boolean def) {
        if (str == null) {
            return def;
        }
        str = str.toLowerCase();
        if (Arrays.asList(new String[]{"true", "yes", "on", "1"}).contains(str)) {
            return true;
        }
        if (Arrays.asList(new String[]{"false", "no", "off", "0"}).contains(str)) {
            return false;
        }
        return def;
    }

    public static Map<String, BigDecimal> getGpsOffset(BigDecimal srcLatitude, int meters) {
        BigDecimal unit = new BigDecimal(111);
        BigDecimal km = new BigDecimal(meters).divide(new BigDecimal(1000), 6, 4);
        BigDecimal latOffset = km.divide(unit, 6, 4);

        double div = Math.cos(srcLatitude.multiply(new BigDecimal(3.141592653589793D).divide(new BigDecimal(180), 6, 4)).doubleValue());
        BigDecimal lngOffset = km.divide(unit.multiply(new BigDecimal(div)), 6, 4);

        Map<String, BigDecimal> result = new HashMap();
        result.put("latOffset", latOffset);
        result.put("lngOffset", lngOffset);

        return result;
    }

    public static boolean hasValue(BigDecimal bigDecimal) {
        return (bigDecimal != null) && (bigDecimal.doubleValue() != 0.0D);
    }

    public static double getGpsDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        BigDecimal lat = ((BigDecimal) add(new BigDecimal[]{lat1, lat2})).divide(new BigDecimal(2), 6, 4);
        BigDecimal offsetLatMeter = subtract(lat1, new BigDecimal[]{lat2}).abs().multiply(new BigDecimal(111000));
        BigDecimal offsetLngMeter = subtract(lng1, new BigDecimal[]{lng2}).abs().multiply(new BigDecimal(111000)).multiply(new BigDecimal(Math.cos(lat.multiply(new BigDecimal(3.141592653589793D).divide(new BigDecimal(180), 6, 4)).doubleValue())));
        return Math.sqrt(multiply(new BigDecimal[]{offsetLatMeter, offsetLatMeter}).add(multiply(new BigDecimal[]{offsetLngMeter, offsetLngMeter})).doubleValue());
    }

    public static <T> T add(T... objects) {
        if ((objects == null) || (objects.length == 0)) {
            return null;
        }


        T notNull = null;
        for (T object : objects) {
            if ((notNull = object) != null) {
                break;
            }
        }

        if (notNull == null) {
            return null;
        }


        String className = notNull.getClass().getName();
        switch (className) {
            case "int":
            case "java.lang.Integer":
                Integer sum = Integer.valueOf(0);
                for (Object object : objects) {
                    if (object != null) {
                        sum = Integer.valueOf(sum.intValue() + ((Integer) object).intValue());
                    }
                }
                return (T) sum;

            case "long":
            case "java.lang.Long":
                Long sum1 = Long.valueOf(0L);
                for (Object object : objects) {
                    if (object != null) {
                        sum1 = Long.valueOf(sum1.longValue() + ((Long) object).longValue());
                    }
                }
                return (T) sum1;

            case "double":
            case "java.lang.Double":
                Double sum2 = Double.valueOf(0.0D);
                for (Object object : objects) {
                    if (object != null) {
                        sum2 = Double.valueOf(sum2.doubleValue() + ((Double) object).doubleValue());
                    }
                }
                return (T) sum2;

            case "java.math.BigDecimal":
                BigDecimal sum3 = BigDecimal.ZERO;
                for (Object object : objects) {
                    if (object != null) {
                        sum3 = sum3.add((BigDecimal) object);
                    }
                }
                return (T) sum3;

            case "java.math.BigInteger":
                BigInteger sum4 = BigInteger.ZERO;
                for (Object object : objects) {
                    if (object != null) {
                        sum4 = sum4.add((BigInteger) object);
                    }
                }
                return (T) sum4;
        }

        throw new RuntimeException("不支持的累加类型：" + className);
    }


    public static BigDecimal multiply(BigDecimal... bigDecimals) {
        boolean hasValue = false;
        BigDecimal result = BigDecimal.ONE;
        for (BigDecimal bigDecimal : bigDecimals) {
            if (bigDecimal != null) {
                result = result.multiply(bigDecimal);
                hasValue = true;
            }
        }
        return hasValue ? result : BigDecimal.ZERO;
    }


    public static BigDecimal subtract(BigDecimal minuend, BigDecimal... subtracts) {
        if (minuend == null) {
            minuend = BigDecimal.ZERO;
        }
        for (BigDecimal subtract : subtracts) {
            if (hasValue(subtract)) {
                minuend = minuend.subtract(subtract);
            }
        }
        return minuend;
    }


    public static BigDecimal divide(BigDecimal division, int scale, int roundMode, BigDecimal... divisors) {
        if (!hasValue(division)) {
            return BigDecimal.ZERO;
        }
        for (BigDecimal divisor : divisors) {
            if (divisor == null) {
                divisor = BigDecimal.ONE;
            }
            division = division.divide(divisor, scale, roundMode);
        }
        return division;
    }


    public static BigDecimal divide(BigDecimal division, BigDecimal... divisors) {
        return divide(division, 2, 6, divisors);
    }

}
