package cn.studease.util;

import java.math.BigDecimal;

/**
 * 常量接口
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public interface Constants {
    public static final String EMPTY = "";
    public static final String STAR = "*";
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    public static final String AES = "AES";
    public static final String SHA1PRNG = "SHA1PRNG";
    public static final String MD5 = "MD5";
    public static final char SPACE_CHAR = ' ';
    public static final char U3000 = '　';
    public static final char U177 = '';
    public static final char UFF00 = '＀';
    public static final char UFF5F = '｟';
    public static final String X_REAL_FORWARDED_FOR = "x-real-forwarded-for";
    public static final String X_FORWARDED_FOR = "x-forwarded-for";
    public static final String UNKNOWN = "unknown";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String IS = "is";
    public static final String AND = "&";
    public static final String EQUALS = "=";
    public static final String MINUS = "-";
    public static final String SLASH = "/";
    public static final String AT = "@";
    public static final String UTF_8 = "utf-8";
    public static final String YMD = "yyyyMMdd";
    public static final String YM = "yyyyMM";
    public static final String YYYY = "yyyy";
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String Y_M = "yyyy-MM";
    public static final String M_D = "MM-dd";
    public static final String START_YMD = "startYmd";
    public static final String END_YMD = "endYmd";
    public static final String[] LUNAR_MONTHS = {"", "正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月"};


    public static final String[] LUNAR_DAYS = {"", "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十", "卅一"};


    public static final BigDecimal BIGDECIMAL_ZERO = BigDecimal.ZERO;

    public static final BigDecimal BIGDECIMAL_ONE = BigDecimal.ONE;

    public static final BigDecimal BIGDECIMAL_HUNDRED = new BigDecimal(100);


    public static final String COMMA = ",";


    public static final String COMMA_CHINESE = "，";


    public static final String DOT = ".";


    public static final String E = "E";

    public static final String DOT_ZERO = ".0";

    public static final String DOT_CLASS = ".class";

    public static final String DOLLAR = "$";

    public static final String TRUE = "true";

    public static final String FALSE = "false";

    public static final String SUCCESS = "success";

    public static final String TOTAL = "total";

    public static final String LIST = "list";

    public static final String MSG = "msg";

    public static final String CLASS_OBJECT = "java.lang.Object";

    public static final String CLASS_STRING = "java.lang.String";

    public static final char[] AZ09 = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static final int AZ09_LENGTH = AZ09.length - 1;
    public static final String CREATE_TIME = "creatateTime";
    public static final String GET_CREATE_TIME = "getCreateTime";
    public static final String SELECT = "select";
    public static final String ORDER_BY = "order by";
    public static final String SELECT_COUNT = "select count";
    public static final String SELECT_STAR = "select *";
    public static final String SELECT_COUNT_ONE = "select count(1) ";
    public static final String FROM = "from";
    public static final String CONVERT = "convert";
    public static final String USING_GBK = " using GBK)";
    public static final String Id = "Id";
    public static final String Ids = "Ids";
    public static final String LEFT_BRACKET = "(";
    public static final String NLSSORT = "nlssort(";
    public static final String NLS_SORT = "，'NLS_SORT=SCHINESE_PINYIN_M')";
    public static final String ONE = "1";
    public static final String MOBILE_PHONE_PATTERN = "^[0123456789]+$";
    public static final String EMAIL_PATTERN = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String HOST = "HOST";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=";
    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    public static final String X_REQUESTED_WITH = "X-Requested-With";
}
