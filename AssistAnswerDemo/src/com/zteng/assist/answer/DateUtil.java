package com.zteng.assist.answer;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {


    public static SimpleDateFormat gfbDate = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     * 年-月-日 时-分-秒-毫秒
     */
    public static SimpleDateFormat formatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * 年-月-日 时-分-秒
     */
    public static SimpleDateFormat formatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 年-月
     */
    public static SimpleDateFormat formatYearMonth = new SimpleDateFormat("yyyy-MM");
    /**
     * 年-月
     */
    public static SimpleDateFormat formatYearMonthNoZero = new SimpleDateFormat("yyyy-M");
    /**
     * 年-月-日
     */
    public static SimpleDateFormat formatYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 时-分-秒
     */
    public static SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    /**
     * 时-分
     */
    public static SimpleDateFormat formatTimehm = new SimpleDateFormat("HH:mm");


    /**
     * 月 日 时-分
     */
    public static SimpleDateFormat formatTimeMonthDayhm = new SimpleDateFormat("MM-dd HH:mm");


    public static boolean is_diff20_minutes(long maxTime, long minTime2) {

            if ((maxTime - minTime2) <= (20 * 1000)) {
                return true;
            }
            return false;

    }

    public static boolean is_diff20_minutes(String maxTime, String minTime2) {
        try {
            Date parse = formatFull.parse(maxTime);
            long mxtime = parse.getTime();
            Date parse1 = formatFull.parse(minTime2);
            long mitime = parse1.getTime();
            if ((mxtime - mitime) <= (20 * 1000)) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }


    public static String getHM(long timeNum) {
        formatTime.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return formatTime.format(timeNum);

    }


    public static String getYM(String dateString) {

        //	LogUtil.logI("kpapp format date  "+ dateString);

        String formatDate = getMonth();
        try {
            Date parse = formatFull.parse(dateString);
            formatDate = formatYearMonth.format(parse);
            return formatDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }


    public static String getYMD(String dateString) {
        String formatDate = getDay();
        try {
            Date parse = formatFull.parse(dateString);
            formatDate = formatYearMonthDay.format(parse);
            return formatDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }


    /**
     * calendar 是否在当前时间之后
     *
     * @param calendar
     * @return
     */
    public static boolean isAfterNow(Calendar calendar) {
        Calendar now = GregorianCalendar.getInstance(Locale.CHINA);
        return calendar.after(now);
    }

    /**
     * calendar 是否当前月份
     *
     * @param calendar
     * @return
     */
    public static boolean isCurrentMonth(Calendar calendar) {
        Calendar now = getCalendar(null);
        return getMonth(calendar).equals(getMonth(now));
    }

    /**
     * calendar 是否在当前月份之后
     *
     * @param calendar
     * @return
     */
    public static boolean isAfterCurrentMonth(Calendar calendar) {
        Calendar now = getCalendar(null);
        String monthNow = getMonth(now);
        String monthCalendar = getMonth(calendar);
        Calendar cNow = getCalendar(monthNow);
        Calendar cc = getCalendar(monthCalendar);
        return cc.after(cNow);

    }

    /**
     * calendar 是否在当前月份之前
     *
     * @param calendar
     * @return
     */
    public static boolean isBeforeCurrentMonth(Calendar calendar) {
        Calendar now = getCalendar(null);
        String monthNow = getMonth(now);
        String monthCalendar = getMonth(calendar);
        Calendar cNow = getCalendar(monthNow);
        Calendar cc = getCalendar(monthCalendar);
        return cc.before(cNow);
    }

    public static boolean isNewer(String date1, String date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            Date d1 = formatFull.parse(date1);
            Date d2 = formatFull.parse(date2);
            calendar1.setTime(d1);
            calendar2.setTime(d2);
            return calendar1.after(calendar2);
        } catch (Exception e) {
          
            e.printStackTrace();
            
        }
        return true;
    }

    /**
     * 判断两个时间是否为今天或者明天或者不是同一年
     *
     * @param date1 旧时间
     * @param date2 最近时间
     * @return date1是date2 0：同一天。 1：昨天。 2：不是同一年
     */
    public static boolean isSameDay(String date1, String date2) {
        String day1 = getDay(getCalendar(date1));
        String day2 = getDay(getCalendar(date2));
        return day1.equals(day2);
    }

    public static boolean isToday(long time) {
        return getDay().equals(formatYearMonthDay.format(new Date(time)));
    }

    public static boolean isInWeek(long time) {
        Calendar calendar = GregorianCalendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Calendar now = GregorianCalendar.getInstance(Locale.CHINA);
        return calendar.after(now);
    }

    public static boolean isToday(String time) {
        return !(time == null || time.length() < 10) && getDay().equals(time.substring(0, 10));
    }

    public static Calendar getCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format;
        if (time == null || time.isEmpty()) {
            return calendar;
        }
        if (time.length() <= "yyyy-MM".length()) {
            format = formatYearMonth;
        } else if (time.length() <= "yyyy-MM-dd".length()) {
            format = formatYearMonthDay;
        } else {
            format = formatFull;
        }
        try {
            Date date = format.parse(time);
            calendar.setTime(date);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return calendar;
    }

    public static String getDateInfo(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    public static String getDateInfo(Calendar calendar, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());
    }


    /**
     * 获取 年-月-日 时：分：秒 字符串信息
     *
     * @return
     */
    public static String getDate() {
        String date = formatFull.format(new Date());
        return date;
    }

    /**
     * 获取 年-月-日 时：分：秒 字符串信息
     *
     * @return
     */
    public static String getDate(Calendar calendar) {
        String date = formatFull.format(calendar.getTime());
        return date;
    }

    /**
     * 获取  年-月-日 时：分：秒 ：毫秒 字符串信息
     *
     * @return
     */
    public static String getFullDate() {
        String date = formatAll.format(new Date());
        return date;
    }

    /**
     * 获取  年-月-日 时：分：秒 ：毫秒 字符串信息
     *
     * @return
     */
    public static String getFullDate(Calendar calendar) {
        String date = formatAll.format(calendar.getTime());
        return date;
    }

    /**
     * 获取  年-月-日 字符串信息
     *
     * @return
     */
    public static String getDay() {
        String date = formatYearMonthDay.format(new Date());
        return date;
    }

    /**
     * 获取  年-月-日 字符串信息
     *
     * @return
     */
    public static String getDay(Calendar calendar) {
        String date = formatYearMonthDay.format(calendar.getTime());
        return date;
    }

    /**
     * 获取  年-月 字符串信息
     *
     * @return
     */
    public static String getMonth() {
        String date = formatYearMonth.format(new Date());
        return date;
    }

    /**
     * 获取  年-月 字符串信息
     *
     * @return
     */
    public static String getMonth(Calendar calendar) {
        String date = formatYearMonth.format(calendar.getTime());
        return date;
    }


    /**
     * 获取 时-分-秒 字符串信息
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        String date = dateFormat.format(new Date());
        return date;
    }

    /**
     * 获取 时-分-秒 字符串信息
     *
     * @return
     */
    public static String getTime(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

    /**
     * 根据当前年份返回大小为length大小的数组，年份偏移量为当前年份+add
     *
     * @param length 总年份数量
     * @param add    年份偏移量
     * @return 装有年份的数组，大小为length，最大年份为当前年份+add
     */
    public static String[] getDateList(int length, int add) {
        String[] date = new String[length];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        int firstYear = currentYear + add;
        for (int i = 0; i < length; i++) {
            date[i] = firstYear + "";
            firstYear--;
        }
        return date;
    }

    public static String[] getDataByMinMax(int min, int max) {
        int count = max - min;
        return getDataByCount(min, count);
    }

    public static String[] getDataByCount(int start, int count) {
        String[] date = new String[count];
        for (int i = 0; i < count; i++) {
            date[i] = String.valueOf(start + i);
        }
        return date;
    }


    /**
     * 判断一个时间是否在某个时间区间中
     *
     * @param date    要比较时间
     * @param begin   开始时间 如： 09:00
     * @param end     结束时间如 ： 23:30
     * @param pattern 开始时间和结束时间对应的格式 "HH:mm"
     * @return
     * @throws ParseException
     */
    public static boolean isInTimeZone(Date date, String begin, String end,
                                       String pattern) throws ParseException {
        if (date == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String srcStr = sdf.format(date);
        long srcTime = sdf.parse(srcStr).getTime();
        long endTime = sdf.parse(end).getTime();
        long beginTime = sdf.parse(begin).getTime();
        if (endTime - srcTime >= 0 && srcTime - beginTime >= 0) {
            return true;
        }

        return false;

    }


    /**
     * 当天显示 时分
     * 昨天显示 昨天 时分
     * 其他时间显示 月日 时分
     *
     * @param time
     */
    public static String getChatTime(String time) throws ParseException {

        if (time != null && time.length()>0) {
          
        
        Date parse = formatFull.parse(time);
        if (isToday(parse)) {
            return formatTimehm.format(parse);
        } else if (isYesterday(parse)) {
            return "昨天 " + formatTimehm.format(parse);
        } else {
            return formatTimeMonthDayhm.format(parse);
        }
        }else{
        	  return getDate();
        }
    }


    /**
     * 判断两个时间相差是否在 多少分钟之内
     *
     * @param smallTime
     * @param bigTime
     * @param minuteDiff
     * @return
     * @throws ParseException
     */
    public static boolean isIn(String smallTime, String bigTime, long minuteDiff) throws ParseException {
        
        Date parse = formatFull.parse(bigTime);
        Date parse2 = formatFull.parse(smallTime);
        long abs = Math.abs(parse.getTime() - parse2.getTime());
        long diff = minuteDiff * 60L * 1000L;
        if (abs <= diff) {
            return true;
        }
        return false;
    }


    public static boolean isWithInaWeek(String date) throws ParseException {
        Date parse = formatFull.parse(date);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -3);
        Date time = instance.getTime();
        if (parse.getTime() > time.getTime()) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否是昨天
     */
    public static boolean isYesterday(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = instance.getTime();
        return isBetween(date, getDateStart(yesterday), getDateEnd(yesterday));

    }


    /**
     * 判断某个时间是否为今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Date today = new Date();
        return isBetween(date, getDateStart(today), getDateEnd(today));
    }


    public static boolean isBetween(Date src, Date begin, Date end) {
        if (src == null) {
            return false;
        }
        if (src.getTime() >= begin.getTime() && src.getTime() <= end.getTime()) {
            return true;
        }
        return false;
    }


    public static String formatYM(String time) throws ParseException {

        Date parse = formatFull.parse(time);
        SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月");
        return s.format(parse);
    }


    public static Date getDateStart(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return (Date) cal.getTime().clone();
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return (Date) cal.getTime().clone();
    }


}
