package cn.studease.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lunar {
    public static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};

    public static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    public static final String[] SHENG_XIAO = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    public static final String[] JIE_QI = {"小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};

    public static final String[] lunarString1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    public static final String[] lunarString2 = {"初", "十", "廿", "卅", "正", "腊", "冬", "闰"};

    private static final int[] lunarInfo = {19416, 19168, 42352, 21717, 53856, 55632, 21844, 22191, 39632, 21970, 19168, 42422, 42192, 53840, 53909, 46415, 54944, 44450, 38320, 18807, 18815, 42160, 46261, 27216, 27968, 43860, 11119, 38256, 21234, 18800, 25958, 54432, 59984, 27285, 23263, 11104, 34531, 37615, 51415, 51551, 54432, 55462, 46431, 22176, 42420, 9695, 37584, 53938, 43344, 46423, 27808, 46416, 21333, 19887, 42416, 17779, 21183, 43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616, 46400, 46752, 38310, 38335, 18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984, 27480, 23232, 43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780, 44368, 21977, 19360, 42416, 20854, 21183, 43312, 31060, 27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200, 30371, 38608, 19195, 19152, 42192, 53430, 53855, 54560, 56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 45653, 27951, 44448, 19299, 37759, 18936, 18800, 25776, 26790, 59999, 27424, 42692, 43759, 37600, 53987, 51552, 54615, 54432, 55888, 23893, 22176, 42704, 21972, 21200, 43448, 43344, 46240, 46758, 44368, 21920, 43940, 42416, 21168, 45683, 26928, 29495, 27296, 44368, 19285, 19311, 42352, 21732, 53856, 59752, 54560, 55968, 27302, 22239, 19168, 43476, 42192, 53584, 62034, 54560};


    private static final int[] solarTermInfo = {0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758};


    private static final String[] sFtv = {"0101*元旦", "0214 情人节", "0308 妇女节", "0312 植树节", "0315 消费者权益日", "0401 愚人节", "0501*劳动节", "0504 青年节", "0509 郝维节", "0512 护士节", "0601 儿童节", "0701 建党节 香港回归纪念", "0801 建军节", "0808 父亲节", "0816 燕衔泥节", "0909 毛泽东逝世纪念", "0910 教师节", "0928 孔子诞辰", "1001*国庆节", "1006 老人节", "1024 联合国日", "1111 光棍节", "1112 孙中山诞辰纪念", "1220 澳门回归纪念", "1225 圣诞节", "1226 毛泽东诞辰纪念"};


    private static final String[] lFtv = {"0101*春节、弥勒佛诞", "0106 定光佛诞", "0115 元宵节", "0208 释迦牟尼佛出家", "0215 释迦牟尼佛涅槃", "0209 海空上师诞", "0219 观世音菩萨诞", "0221 普贤菩萨诞", "0316 准提菩萨诞", "0404 文殊菩萨诞", "0408 释迦牟尼佛诞", "0415 佛吉祥日——释迦牟尼佛诞生、成道、涅槃三期同一庆(即南传佛教国家的卫塞节)", "0505 端午节", "0513 伽蓝菩萨诞", "0603 护法韦驮尊天菩萨诞", "0619 观世音菩萨成道——此日放生、念佛，功德殊胜", "0707 七夕情人节", "0713 大势至菩萨诞", "0715 中元节", "0724 龙树菩萨诞", "0730 地藏菩萨诞", "0815 中秋节", "0822 燃灯佛诞", "0909 重阳节", "0919 观世音菩萨出家纪念日", "0930 药师琉璃光如来诞", "1005 达摩祖师诞", "1107 阿弥陀佛诞", "1208 释迦如来成道日，腊八节", "1224 小年", "1229 华严菩萨诞", "0100*除夕"};


    private static final Pattern sFreg = Pattern.compile("^(\\d{2})(\\d{2})([\\s\\*])(.+)$");

    private static final Pattern wFreg = Pattern.compile("^(\\d{2})(\\d)(\\d)([\\s\\*])(.+)$");


    private static String[] wFtv = {"0520 母亲节", "0716 合作节", "0730 被奴役国家周"};

    private static GregorianCalendar utcCal = null;

    private boolean isFinded = false;

    private boolean isSFestival = false;

    private boolean isLFestival = false;

    private String sFestivalName = "";

    private String lFestivalName = "";

    private String description = "";

    private boolean isHoliday = false;

    private Calendar solar;

    private int lunarYear;

    private int lunarMonth;

    private int lunarDay;

    private boolean isLeap;

    private boolean isLeapYear;

    private int solarYear;

    private int solarMonth;

    private int solarDay;

    private int cyclicalYear = 0;

    private int cyclicalMonth = 0;

    private int cyclicalDay = 0;

    private int maxDayInMonth = 29;


    public Lunar(Date date) {
        if (date == null)
            date = new Date();
        init(date.getTime());
    }


    public Lunar(long TimeInMillis) {
        init(TimeInMillis);
    }

    public static void main(String[] args) {
        Lunar l = new Lunar(System.currentTimeMillis());
        System.out.println("节气:" + l.getTermString());
        System.out.println("干支历:" + l.getCyclicalDateString());
        System.out.println("星期" + l.getDayOfWeek());
        System.out.println("农历" + l.getLunarDateString());
    }

    private static int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return -1;
    }


    private static int getLunarLeapMonth(int lunarYear) {
        int leapMonth = lunarInfo[(lunarYear - 1900)] & 0xF;
        leapMonth = leapMonth == 15 ? 0 : leapMonth;
        return leapMonth;
    }


    private static int getLunarLeapDays(int lunarYear) {
        return getLunarLeapMonth(lunarYear) > 0 ? 29 : (lunarInfo[(lunarYear - 1899)] & 0xF) == 15 ? 30 : 0;
    }


    private static int getLunarYearDays(int lunarYear) {
        int daysInLunarYear = 348;


        for (int i = 32768; i > 8; i >>= 1) {
            daysInLunarYear += ((lunarInfo[(lunarYear - 1900)] & i) != 0 ? 1 : 0);
        }

        daysInLunarYear += getLunarLeapDays(lunarYear);

        return daysInLunarYear;
    }


    private static int getLunarMonthDays(int lunarYear, int lunarMonth) {
        return (lunarInfo[(lunarYear - 1900)] & 65536 >> lunarMonth) != 0 ? 30 : 29;
    }


    public static synchronized int getUTCDay(Date date) {


        synchronized (utcCal) {
            utcCal.clear();
            utcCal.setTimeInMillis(date.getTime());
            return utcCal.get(5);
        }
    }

    private static synchronized void makeUTCCalendar() {
        if (utcCal == null) {
            utcCal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        }
    }


    public static synchronized long UTC(int year, int month, int day, int hour, int minute, int second) {


        synchronized (utcCal) {
            utcCal.clear();
            utcCal.set(year, month, day, hour, minute, second);
            return utcCal.getTimeInMillis();
        }
    }


    private static int getSolarTermDay(int solarYear, int index) {
        long l = 31556925974L * (solarYear - 1900) + solarTermInfo[index] * 60000L;
        l += UTC(1900, 0, 6, 2, 5, 0);
        return getUTCDay(new Date(l));
    }


    private static String getCyclicalString(int cyclicalNumber) {
        return TIAN_GAN[getTianan(cyclicalNumber)] + DI_ZHI[getDeqi(cyclicalNumber)];
    }


    private static int getDeqi(int cyclicalNumber) {
        return cyclicalNumber % 12;
    }


    private static int getTianan(int cyclicalNumber) {
        return cyclicalNumber % 10;
    }


    private static String getLunarYearString(int lunarYear) {
        return getCyclicalString(lunarYear - 1900 + 36);
    }


    private static String getLunarMonthString(int lunarMonth) {
        String lunarMonthString = "";
        if (lunarMonth == 1) {
            lunarMonthString = lunarString2[4];
        } else {
            if (lunarMonth > 9)
                lunarMonthString = lunarMonthString + lunarString2[1];
            if (lunarMonth % 10 > 0)
                lunarMonthString = lunarMonthString + lunarString1[(lunarMonth % 10)];
        }
        return lunarMonthString;
    }


    private static String getLunarDayString(int lunarDay) {
        if ((lunarDay < 1) || (lunarDay > 30))
            return "";
        int i1 = lunarDay / 10;
        int i2 = lunarDay % 10;
        String c1 = lunarString2[i1];
        String c2 = lunarString1[i2];
        if (lunarDay < 11)
            c1 = lunarString2[0];
        if (i2 == 0)
            c2 = lunarString2[1];
        return c1 + c2;
    }

    private synchronized void findFestival() {
        int sM = getSolarMonth();
        int sD = getSolarDay();
        int lM = getLunarMonth();
        int lD = getLunarDay();
        int sy = getSolarYear();

        for (int i = 0; i < sFtv.length; i++) {
            Matcher m = sFreg.matcher(sFtv[i]);
            if ((m.find()) &&
                    (sM == toInt(m.group(1))) && (sD == toInt(m.group(2)))) {
                this.isSFestival = true;
                this.sFestivalName = m.group(4);
                if (!"*".equals(m.group(3))) break;
                this.isHoliday = true;
                break;
            }
        }


        for (int i = 0; i < lFtv.length; i++) {
            Matcher m = sFreg.matcher(lFtv[i]);
            if ((m.find()) &&
                    (lM == toInt(m.group(1))) && (lD == toInt(m.group(2)))) {
                this.isLFestival = true;
                this.lFestivalName = m.group(4);
                if (!"*".equals(m.group(3))) break;
                this.isHoliday = true;
                break;
            }
        }


        for (int i = 0; i < wFtv.length; i++) {
            Matcher m = wFreg.matcher(wFtv[i]);
            if ((m.find()) &&
                    (getSolarMonth() == toInt(m.group(1)))) {
                int w = toInt(m.group(2));
                int d = toInt(m.group(3));
                if ((this.solar.get(4) == w) && (this.solar.get(7) == d)) {
                    this.isSFestival = true;
                    this.sFestivalName = (this.sFestivalName + "|" + m.group(5));
                    if ("*".equals(m.group(4))) {
                        this.isHoliday = true;
                    }
                }
            }
        }
        if ((sy > 1874) && (sy < 1909))
            this.description = ("光绪" + (sy - 1874 == 1 ? "元" : new StringBuilder().append("").append(sy - 1874).toString()));
        if ((sy > 1908) && (sy < 1912))
            this.description = ("宣统" + (sy - 1908 == 1 ? "元" : String.valueOf(sy - 1908)));
        if ((sy > 1911) && (sy < 1950))
            this.description = ("民国" + (sy - 1911 == 1 ? "元" : String.valueOf(sy - 1911)));
        if (sy > 1949)
            this.description = ("共和国" + (sy - 1949 == 1 ? "元" : String.valueOf(sy - 1949)));
        this.description += "年";
        this.sFestivalName = this.sFestivalName.replaceFirst("^\\|", "");
        this.isFinded = true;
    }

    private void init(long TimeInMillis) {
        this.solar = Calendar.getInstance();
        this.solar.setTimeInMillis(TimeInMillis);
        Calendar baseDate = new GregorianCalendar(1900, 0, 31);
        long offset = (TimeInMillis - baseDate.getTimeInMillis()) / 86400000L;

        this.lunarYear = 1900;
        int daysInLunarYear = getLunarYearDays(this.lunarYear);
        while ((this.lunarYear < 2100) && (offset >= daysInLunarYear)) {
            offset -= daysInLunarYear;
            daysInLunarYear = getLunarYearDays(++this.lunarYear);
        }


        int lunarMonth = 1;

        int leapMonth = getLunarLeapMonth(this.lunarYear);

        this.isLeapYear = (leapMonth > 0);

        boolean leapDec = false;
        boolean isLeap = false;
        int daysInLunarMonth = 0;
        while ((lunarMonth < 13) && (offset > 0L)) {
            if ((isLeap) && (leapDec)) {
                daysInLunarMonth = getLunarLeapDays(this.lunarYear);
                leapDec = false;
            } else {
                daysInLunarMonth = getLunarMonthDays(this.lunarYear, lunarMonth);
            }
            if (offset < daysInLunarMonth) {
                break;
            }
            offset -= daysInLunarMonth;

            if ((leapMonth == lunarMonth) && (!isLeap)) {
                leapDec = true;
                isLeap = true;
            } else {
                lunarMonth++;
            }
        }
        this.maxDayInMonth = daysInLunarMonth;

        this.lunarMonth = lunarMonth;

        this.isLeap = ((lunarMonth == leapMonth) && (isLeap));

        this.lunarDay = ((int) offset + 1);

        getCyclicalData();
    }


    private void getCyclicalData() {
        this.solarYear = this.solar.get(1);
        this.solarMonth = this.solar.get(2);
        this.solarDay = this.solar.get(5);

        int cyclicalYear = 0;
        int cyclicalMonth = 0;


        int term2 = getSolarTermDay(this.solarYear, 2);

        if ((this.solarMonth < 1) || ((this.solarMonth == 1) && (this.solarDay < term2))) {
            cyclicalYear = (this.solarYear - 1900 + 36 - 1) % 60;
        } else {
            cyclicalYear = (this.solarYear - 1900 + 36) % 60;
        }


        int firstNode = getSolarTermDay(this.solarYear, this.solarMonth * 2);

        if (this.solarDay < firstNode) {
            cyclicalMonth = ((this.solarYear - 1900) * 12 + this.solarMonth + 12) % 60;
        } else {
            cyclicalMonth = ((this.solarYear - 1900) * 12 + this.solarMonth + 13) % 60;
        }


        int cyclicalDay = (int) (UTC(this.solarYear, this.solarMonth, this.solarDay, 0, 0, 0) / 86400000L + 25567L + 10L) % 60;
        this.cyclicalYear = cyclicalYear;
        this.cyclicalMonth = cyclicalMonth;
        this.cyclicalDay = cyclicalDay;
    }


    public String getAnimalString() {
        return SHENG_XIAO[((this.lunarYear - 4) % 12)];
    }


    public String getTermString() {
        String termString = "";
        if (getSolarTermDay(this.solarYear, this.solarMonth * 2) == this.solarDay) {
            termString = JIE_QI[(this.solarMonth * 2)];
        } else if (getSolarTermDay(this.solarYear, this.solarMonth * 2 + 1) == this.solarDay) {
            termString = JIE_QI[(this.solarMonth * 2 + 1)];
        }
        return termString;
    }


    public String getCyclicalDateString() {
        return getCyclicaYear() + "年" + getCyclicaMonth() + "月" + getCyclicaDay() + "日";
    }


    public int getTiananY() {
        return getTianan(this.cyclicalYear);
    }


    public int getTiananM() {
        return getTianan(this.cyclicalMonth);
    }


    public int getTiananD() {
        return getTianan(this.cyclicalDay);
    }


    public int getDeqiY() {
        return getDeqi(this.cyclicalYear);
    }


    public int getDeqiM() {
        return getDeqi(this.cyclicalMonth);
    }


    public int getDeqiD() {
        return getDeqi(this.cyclicalDay);
    }


    public String getCyclicaYear() {
        return getCyclicalString(this.cyclicalYear);
    }


    public String getCyclicaMonth() {
        return getCyclicalString(this.cyclicalMonth);
    }


    public String getCyclicaDay() {
        return getCyclicalString(this.cyclicalDay);
    }


    public String getLunarDayString() {
        return getLunarDayString(this.lunarDay);
    }


    public String getLunarMonthString() {
        return (isLeap() ? "闰" : "") + getLunarMonthString(this.lunarMonth);
    }


    public String getLunarYearString() {
        return getLunarYearString(this.lunarYear);
    }


    public String getLunarDateString() {
        return getLunarYearString() + "年" + getLunarMonthString() + "月" + getLunarDayString() + "日";
    }


    public boolean isLeap() {
        return this.isLeap;
    }


    public boolean isLeapYear() {
        return this.isLeapYear;
    }


    public boolean isBigMonth() {
        return getMaxDayInMonth() > 29;
    }


    public int getMaxDayInMonth() {
        return this.maxDayInMonth;
    }


    public int getLunarDay() {
        return this.lunarDay;
    }


    public int getLunarMonth() {
        return this.lunarMonth;
    }


    public int getLunarYear() {
        return this.lunarYear;
    }


    public int getSolarDay() {
        return this.solarDay;
    }


    public int getSolarMonth() {
        return this.solarMonth + 1;
    }


    public int getSolarYear() {
        return this.solarYear;
    }


    public int getDayOfWeek() {
        return this.solar.get(7);
    }


    public boolean isBlackFriday() {
        return (getSolarDay() == 13) && (this.solar.get(7) == 6);
    }


    public boolean isToday() {
        Calendar clr = Calendar.getInstance();
        return (clr.get(1) == this.solarYear) && (clr.get(2) == this.solarMonth) && (clr.get(5) == this.solarDay);
    }


    public String getSFestivalName() {
        return this.sFestivalName;
    }


    public String getLFestivalName() {
        return this.lFestivalName;
    }


    public boolean isLFestival() {
        if (!this.isFinded)
            findFestival();
        return this.isLFestival;
    }


    public boolean isSFestival() {
        if (!this.isFinded)
            findFestival();
        return this.isSFestival;
    }


    public boolean isFestival() {
        return (isSFestival()) || (isLFestival());
    }


    public boolean isHoliday() {
        if (!this.isFinded)
            findFestival();
        return this.isHoliday;
    }


    public String getDescription() {
        if (!this.isFinded)
            findFestival();
        return this.description;
    }
}


