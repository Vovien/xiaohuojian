/**
 * Copyright (C) 2012 www.amsoft.cn
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jmbon.middleware.utils;

import com.blankj.utilcode.constant.TimeConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import rxhttp.wrapper.utils.LogUtil;

// TODO: Auto-generated Javadoc

/***
 * © 2012 amsoft.cn
 * 描述：日期处理类.
 *
 * @date：2013-01-18 下午11:52:13
 */
public class DateFormatUtil {

    /***
     * 时间日期格式化到年月日时分秒.
     */
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormatY = "yyyy";
    public static final String dateFormatH = "MM";
    public static final String dateFormatYMDDatePicke = "yyyy-M-d";
    /***
     * 时间日期格式化到年月日.
     */
    public static final String dateFormatYMD = "yyyy-MM-dd";
    public static final String datePointFormatYMD = "yyyy.MM.dd";
    public static final String dateFormatYMDTwo = "yyyyMMdd";
    public static final String dateFormatYMDTrain = "yyyy/MM/dd";
    public static final String dateFormatYMDtwo = "yyyy年MM月dd日";
    public static final String dateFormatYMDOne = "yyyy年M月d日";
    public static final String dateFormatYMDThree = "yyyy年MM月dd日 HH:mm";

    public static final String dateFormatYMDFour = "yyyy-MM-dd H点:m分";
    /***
     * 时间日期格式化到年月.
     */
    public static final String dateFormatYM = "yyyy-MM";

    /***
     * 时间日期格式化到年月.
     */
    public static final String dateFormatMDHM = "MM-dd HH:mm";

    /***
     * 时间日期格式化到年月日时分.
     */
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

    /***
     * 时间日期格式化到月日.
     */
    public static final String dateFormatMD = "MM-dd";
    public static final String dateFormatMDSim = "M-d";
    /***
     * 时间日期格式化到月日.
     */
    public static final String dateFormatMDTwo = "MM月dd日";
    public static final String dateFormatMDOne = "M月d日";
    public static final String dateFormatD = "dd日";


    /***
     * 时分秒.
     */
    public static final String dateFormatHMS = "HH:mm:ss";

    /***
     * 时分.
     */
    public static final String dateFormatHM = "HH:mm";

    /***
     * 上午.
     */
    public static final String AM = "AM";

    /***
     * 下午.
     */
    public static final String PM = "PM";


    public static String getHourAndMinute(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatHM);
        return simpleDateFormat.format(date);
    }


    public static String getYearAndMonthAndDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatYMDtwo);
        return simpleDateFormat.format(date);
    }

    /***
     * 描述：计算当前时间是星期几
     *
     * @param
     * @return
     */
    public static String getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

    /***
     * 描述：计算指定时间是星期几
     *
     * @param
     * @return
     */
    public static String getSomeDayOfWeek(long mills) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTimeInMillis(mills);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

    /***
     * 描述：计算指定时间是今年第多少天
     *
     * @param
     * @return
     */
    public static int getIndexDayOfYear(long mills) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTimeInMillis(mills);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        int mDayIndex = calendar.get(Calendar.DAY_OF_YEAR);
        return mDayIndex;
    }

    /***
     * 描述：判断是否属于今年
     *
     * @param
     * @return
     */
    public static boolean isEqualsNowYear(long mills) {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        Calendar targetCalendar = new GregorianCalendar();
        try {
            targetCalendar.setTimeInMillis(mills);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return targetYear == year;
    }

    /***
     * 描述：计算指定时间是今年第多少年
     *
     * @param
     * @return
     */
    public static int getIndexYear(long mills) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTimeInMillis(mills);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        int mYearIndex = calendar.get(Calendar.YEAR);
        return mYearIndex;
    }

    /***
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /***
     * 描述：获取偏移之后的Date.
     *
     * @param date          日期时间
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public static Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /***
     * 描述：获取指定日期时间的字符串(可偏移).
     *
     * @param strDate       String形式的日期时间
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getStringByOffset(String strDate, String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDateTime;
    }


    /***
     * 描述：Date类型转化为String类型(可偏移).
     *
     * @param date          the date
     * @param format        the format
     * @param calendarField the calendar field
     * @param offset        the offset
     * @return String String类型日期时间
     */
    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    /***
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static String getStringByFormat(String strDate, String format, String toFormat) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(toFormat);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     * MMM dd,yyyy kk:mm:ss aa
     *
     * @param strDate the date
     * @param format  the format
     * @return String String类型日期时间
     */
    public static String getStringByDateFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MMM dd,yyyy kk:mm:ss aa", Locale.ENGLISH);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormatThree(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMD);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    public static String getStringByFormatFour(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHM);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormatTwo(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMD);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：获取当天时间，
     */
    public static String getStringNow() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMD);
        String now = mSimpleDateFormat.format(new Date());
        return now;
    }

    /***
     * 描述：获取当天时间，
     */
    public static String getDateStringNow() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDTwo);
        String now = mSimpleDateFormat.format(new Date());
        return now;
    }

    /***
     * 描述：将时间戳转换成指定格式的字符串.
     *
     * @param milliseconds the milliseconds
     * @param format       格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    public static String getMD(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatMDTwo);
    }


    public static String getDateFormatMDHM(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatMDHM);
    }

    public static String getMDTwo(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormatThree(newDate, DateFormatUtil.dateFormatMDTwo);
    }

    public static String getMDNoChinese(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatMD);
    }

    public static String getHM(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatHM);
    }

    public static String getYMD(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatYMD);
    }

    public static String getYMDChinese(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatYMDtwo);
    }

    public static String getYMDHMS(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatYMDHMS);
    }

    public static String getYMDHM(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatYMDHM);
    }

    public static String getY(String date) {
        String newDate = date.replaceAll("T", " ");
        return getStringByFormat(newDate, DateFormatUtil.dateFormatY);
    }

    public static int getCurrentDateHH(String date) {
        String h = getCurrentDate(dateFormatH);
        if (h.startsWith("0") && h.length() == 2) {
            return Integer.parseInt(h.substring(1, 2));
        }
        return Integer.parseInt(h);
    }

    /***
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static String getCurrentDate(String format) {
//        LogUtil.d(DateFormatUtil.class, "getCurrentDate:" + format);
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }

    /***
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 描述：获取表示当前日期时间的字符串(可偏移).
     *
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：获取表示当前日期时间的字符串(可偏移).
     *
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getDateByOffsetDay(String dateStr, String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((DateFormatUtil.getLongByFormat(dateStr)));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /***
     * 描述：计算两个日期所差的天数.
     *
     * @param milliseconds1 the milliseconds1
     * @param milliseconds2 the milliseconds2
     * @return int 所差的天数
     */
    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /***
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1 - h2 + day * 24;
        return h;
    }

    /***
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }

    /***
     * 描述：获取本周一.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.MONDAY);
    }

    /***
     * 描述：获取本周日.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.SUNDAY);
    }

    /***
     * 描述：获取本周的某一天.
     *
     * @param format        the format
     * @param calendarField the calendar field
     * @return String String类型日期时间
     */
    private static String getDayOfWeek(String format, int calendarField) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            } else {
                int offectDay = calendarField - week;
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay);
                }
                c.add(Calendar.DATE, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static long getLongByFormat(String time) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//       String time="1970-01-061 1:45:55";//注：改正后这里前后也加了空格
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getLongByFormatTwo(String time, String dateFormat) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
//       String time="1970-01-061 1:45:55";//注：改正后这里前后也加了空格
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /***
     * 描述：获取本月第一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            //当前月的第一天
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /***
     * 描述：获取本月最后一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的最后一天
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    /***
     * 描述：获取表示当前日期的0点时间毫秒数.
     *
     * @return the first time of day
     */
    public static long getFirstTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /***
     * 描述：获取表示当前日期的0点时间毫秒数.
     *
     * @return the first time of day
     */
    public static long getCurrTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate, dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /***
     * 描述：获取表示当前日期24点时间毫秒数.
     *
     * @return the last time of day
     */
    public static long getLastTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /***
     * 描述：判断是否是闰年()
     * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     *
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 || year % 400 == 0;
    }


    /***
     * 取指定日期为星期几.
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return String   星期几
     */
    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "周日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            return "错误";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "周日";
                break;
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
        }
        return week;
    }

    /***
     * 取指定日期为星期几.
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return String   星期几
     */
    public static String getWeekNumberTwo(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            return "错误";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }

    /***
     * 根据给定的日期判断是否为上下午.
     *
     * @param strDate the str date
     * @param format  the format
     * @return the time quantum
     */
    public static String getTimeQuantum(String strDate, String format) {
        Date mDate = getDateByFormat(strDate, format);
        int hour = mDate.getHours();
        if (hour >= 12) {
            return "PM";
        } else {
            return "AM";
        }
    }


    /***
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
//        System.out.println(formatDateStr2Desc("2012-3-2 12:2:20", "MM月dd日  HH:mm"));
    }

    public static String minuteToHour(int minute) {
        int hour = minute / 60;
        int mMinute = minute % 60;
        return hour + "时" + mMinute + "分";
    }

    public static String secondToHour(int totalSeconds) {
        if (totalSeconds == 0 || totalSeconds < 1) {
            return "00:00";
        }
        //将秒格式化成HH:mm:ss
        //这里应该用Duration更合理，但它不能格式化成字符串
        //而使用LocalTime，在时间超过24小时后格式化也会有问题（！）
        int hours = totalSeconds / 3600;

        int rem = totalSeconds % 3600;
        int minutes = rem / 60;
        int seconds = rem % 60;
        if (hours <= 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    /***
     * 时间的加减操作
     *
     * @param flag   加减的类型
     * @param how    加减的数
     * @param time   指定的时间，格式为 yyyy-MM-dd HH:mm
     * @param format 指定的格式
     * @return
     */
    public static String getAtTime(int flag, int how, String time, String format) {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            gc.setTime(sf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gc.add(flag, +how);
        gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE), gc.get(Calendar.HOUR_OF_DAY), gc.get(Calendar.MINUTE));
        String newTime = sf.format(gc.getTime());
        return newTime;
    }


    /**
     * Return the friendly time span by now.
     *
     * @param millis The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * <li>如果小于 1 秒钟内，显示刚刚</li>
     * <li>如果在 1 分钟内，显示 XXX秒前</li>
     * <li>如果在 1 小时内，显示 XXX分钟前</li>
     * <li>如果在 1 小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(final long millis) {
        if (millis == 0)
            return "";
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
            //return String.format("%tc", millis);
            //服务器和本地时间差为负也返回 刚刚
            return "刚刚";
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        }
        // 获取当天 00:00
        long wee = getWeeOfToday();
        if (millis >= wee) {
            return String.format("今天%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天%tR", millis);
        } else {
            return String.format("%tF", millis);
        }
    }

    /**
     * Return the friendly time span by now.
     *
     * @param millis The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * <li>如果小于 1 秒钟内，显示刚刚</li>
     * <li>如果在 1 分钟内，显示 XXX秒前</li>
     * <li>如果在 1 小时内，显示 XXX分钟前</li>
     * <li>如果在 1 小时外的今天内，显示xx小时前 （24小时）</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>今年内，10月15日</li>
     * <li>其余显示，2016年10月15日</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow2(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
            // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
            //return String.format("%tc", millis);
            //服务器和本地时间差为负也返回 刚刚
            return "刚刚";
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        }
        // 获取当天 00:00
        long wee = getWeeOfToday();
        int dayYear = isLeapYear(getIndexYear(millis)) ? 366 : 365;
        if (millis >= wee) {
            return String.format(Locale.getDefault(), "%d小时前", span / TimeConstants.HOUR);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天%tR", millis);
        } else if (isEqualsNowYear(millis)) {
            //今年内
            return getStringByFormat(millis, dateFormatMDOne);
        } else {
            return getStringByFormat(millis, dateFormatYMDOne);
        }
    }

    /**
     * Return the friendly time span by now.
     * 1天以内（以当前时间计算天数） 直接显示具体时间 (xx:xx)
     * 昨天以内 显示样式：昨天+具体时间
     * 3-7天内的消息（包含3、7） 显示样式：星期X+具体时间
     * 大于7天 显示样式：XXXX年X月X日+具体时间
     */
    public static String getChatFriendlyTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();

        // 获取当天 00:00
        long wee = getWeeOfToday();
        if (millis >= wee) {
            return String.format("%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天 %tR", millis);
        } else if (millis >= wee - TimeConstants.DAY * 7) {
            return String.format("%s %tR", getSomeDayOfWeek(millis), millis);
        } else {
            return getStringByFormat(millis, dateFormatYMDThree);
        }
    }

    /**
     * Return the friendly time span by now.
     *
     * @param millis The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * 1天以内（以当前时间计算天数） 直接显示具体时间 (xx:xx)
     * 昨天以内 显示样式：昨天
     * 3-7天内的消息（包含3、7） 显示样式：星期X
     * 大于7天 显示样式：XXXX年X月X日
     * </ul>
     */
    public static String getChatFriendlyTimeSpanByNow2(final long millis) {
        long now = System.currentTimeMillis();
        // 获取当天 00:00
        long wee = getWeeOfToday();
        if (millis >= wee) {
            return String.format("%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            // return String.format("昨天 %tR", millis);
            return "昨天";
        } else if (millis >= wee - TimeConstants.DAY * 7) {
            return getSomeDayOfWeek(millis);
        } else {
            return getStringByFormat(millis, dateFormatYMDOne);
        }
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取 关注列表/视频详情 时间格式
     *
     * @param millis The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * 1天以内（以当前时间计算天数） 直接显示具体时间 (xx:xx)
     * 昨天以内 显示样式：昨天+具体时间
     * 3-7天内的消息（包含3、不包含7） 显示样式：星期X
     * 大于7天且今年内 显示样式：12-1
     * 今年前 2021-12-1
     * </ul>
     */
    public static String getFollowTime(final long millis) {
        long now = System.currentTimeMillis();
        // 获取当天 00:00
        long wee = getWeeOfToday();

        int dayYear = isLeapYear(getIndexYear(millis)) ? 366 : 365;
        if (millis >= wee) {
            return String.format("%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天%tR", millis);
        } else if (millis > (wee - TimeConstants.DAY * (6-1))) {
            //3到7天内
            return getSomeDayOfWeek(millis);
        } else if (isEqualsNowYear(millis)) {
            //今年内
            return getStringByFormat(millis, dateFormatMDSim);
        } else {
            //今年前
            return getStringByFormat(millis, dateFormatYMDDatePicke);
        }
    }

}
