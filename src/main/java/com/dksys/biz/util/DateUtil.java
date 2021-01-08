package com.dksys.biz.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil extends DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static final String YYYYMM               = "yyyyMM";
    public static final String YYYYMMDD               = "yyyyMMdd";
    public static final String YYYYMMDDHH             = "yyyyMMddHH";
    public static final String YYYYMMDDHHMM           = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMMSS         = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS      = "yyyyMMddHHmmssSSS";
    public static final String YYYY_MM_DD             = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HHMM       = "yyyy-MM-dd HHmm";
    public static final String YYYY_MM_DD_HH_MM       = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDD_HHMMSS        = "yyyy-MM-dd HH：mm：ss"; // 아래꺼와 콜론(:)이 서로 다름 
    public static final String YYYYMMDD_HHMMSS2        = "yyyy-MM-dd HH:mm:ss";// 위에꺼와 콜론(：)이 서로 다름 
    public static final String YYYYMMDD_HHMMSS_SSS    = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYYMMDD_HHMMSS_KOREAN = "yyyy년 MM월 dd일 HH시mm분ss초";
    public static final String YYYYMMDD_HHMMSS_AMPM = "yyyy-MM-dd a hh:mm:ss";
    public static final String HHMMSS                 = "HHmmss";
    
    public static String dateToString(Date obj, String format) {
        if (obj == null)
            return "";
        SimpleDateFormat dtFormatter = new SimpleDateFormat(format);
        return dtFormatter.format(obj);
    }
    public static Date stringToDate(String strDate, String format) throws ParseException {
        SimpleDateFormat dtFormatter = new SimpleDateFormat(format);
        if (StringUtil.isEmpty(strDate))
            throw new ParseException("Cannot convert empty string to Date.", 0);
        return dtFormatter.parse(strDate.trim());
    }
    public static Date convertFlexibleDate(String strDate, String[] formats) throws ParseException {
        if (StringUtil.isEmpty(strDate))
            return null;
        for (int i = 0; i < formats.length; i++) {
            try {
                SimpleDateFormat dtFormatter = new SimpleDateFormat(formats[i]);
                dtFormatter.setLenient(false);
                return dtFormatter.parse(strDate.trim());
            } catch (ParseException e) {
                // do nothing... try other format
            }
        }
        // we are unable to convert
        throw new ParseException("No matching date format for " + strDate, 0);
    }
    /**
     * default 는 영문(AM/PM) 으로 표기한다.
     * @param obj
     * @param objs
     * @return
     */
    public static String getDateStrAMPM(Date obj) {
        SimpleDateFormat dtFormatter = new SimpleDateFormat(YYYYMMDD_HHMMSS_AMPM,Locale.ENGLISH);
        return dtFormatter.format(obj);
    }
    public static String getDateStrAMPM_Korean(Date obj) {
        SimpleDateFormat dtFormatter = new SimpleDateFormat(YYYYMMDD_HHMMSS_AMPM,Locale.KOREAN);
        return dtFormatter.format(obj);
    }
    public static Date getToday() {
        return new Date();
    }
    public static String getToday(String format) {
        return dateToString(new Date(), format);
    }
    public static String getTodayYYYYMMDD() {
        return dateToString(new Date(), YYYYMMDD);
    }
    public static String getTodayYYYYMM() {
        return dateToString(new Date(), YYYYMM);
    }
    public static String getTodayHHMMSS() {
        return dateToString(new Date(), HHMMSS);
    }
    public static String getTodayYYYYMMDDHH() {
        return dateToString(new Date(), YYYYMMDDHH);
    }
    public static int getTodayIntYYYYMMDD() {
        return Integer.parseInt(getTodayYYYYMMDD());
    }
    public static String convertShortDate(Date obj) {
        return dateToString(obj, YYYYMMDD);
    }
    public static Date convertShortDate(String str) throws ParseException {
        return stringToDate(str, YYYYMMDD);
    }
    public static Date convertShortDate(String str, Date defaultDate) {
        try {
            return stringToDate(str, YYYYMMDD);
        } catch (ParseException pex) {
            return defaultDate;
        }
    }
    public static Date convertDate(String strDate, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            formatter.setLenient(false);
            return formatter.parse(strDate.trim());
        } catch (ParseException pex) {
            return null;
        }
    }
    public static Date convertFlexibleDate(String strDate) throws ParseException {
        if (StringUtil.isEmpty(strDate))
            throw new ParseException("Cannot convert empty string to Date.", 0);
        String[] formats = { "MM/dd/yyyy", "MM-dd-yyyy", "yyyy-MM-dd", "MMM dd yyyy", "MMM dd, yyyy", "MMM yyyy", "MM/yyyy", "MM-yyyy", "yyyy", 
                YYYYMMDD, YYYYMMDDHH, YYYYMMDDHHMM, YYYYMMDDHHMMSS, YYYYMMDDHHMMSSSSS, YYYYMMDD_HHMMSS, YYYY_MM_DD_HHMM, YYYYMMDD_HHMMSS_SSS ,YYYYMMDD_HHMMSS2, YYYYMMDD_HHMMSS_KOREAN,  YYYYMMDD_HHMMSS_AMPM};
        return convertFlexibleDate(strDate, formats);
    }
    public static boolean compareNullableDates(Date date1, Date date2) {
        if ((date1 == null) && (date2 == null))
            return true;
        if (date1 != null) {
            if (date1.equals(date2))
                return true;
            else
                return false;
        }
        return false;
    }
    /**
     * Checks if the given date has a time recorded or just plain 00:00:00.000
     *
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean hasTime(Date date) {
        return (date.getHours() + date.getMinutes() + date.getSeconds() > 0);
    }
    /**
     * Get current date according to client's time zone.
     *
     * @param calendar
     *            - adapting calendar
     * @param timeZone
     *            - client time zone
     * @return adapt calendar to client time zone
     */
    public static Date getClientCurrentDate(final Calendar calendar, final TimeZone timeZone) {
        Calendar result = new GregorianCalendar(timeZone);
        result.setTimeInMillis(calendar.getTimeInMillis() + timeZone.getOffset(calendar.getTimeInMillis()) - TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
        result.getTime();
        return result.getTime();
    }
    public static final int YEAR       = 1;
    public static final int MONTH      = 2;
    public static final int DATE       = 3;
    public static final int MonTHFIRST = 4;
    public static final int MONTHEND   = 5;
    public static String getYyyymmdd(Calendar cal) {
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = YYYYMMDD;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(cal.getTime());
    }
    public static GregorianCalendar getGregorianCalendar(String yyyymmdd) {
        int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int dd = Integer.parseInt(yyyymmdd.substring(6));
        GregorianCalendar calendar = new GregorianCalendar(yyyy, mm - 1, dd, 0, 0, 0);
        return calendar;
    }
    public static String getCurrentDateTime() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = YYYYMMDDHHMMSS;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }
    public static String getCurrentTime() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = HHMMSS;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }
    public static String getCurrentYyyymmdd() {
        return getCurrentDateTime().substring(0, 8);
    }
    public static String getCurrentYyyymm() {
        return getCurrentDateTime().substring(0, 6);
    }
    public static String getCurrentYyyy() {
        return getCurrentDateTime().substring(0, 4);
    }
    public static String getCurrentMm() {
        return getCurrentDateTime().substring(4, 6);
    }
    public static String getWeekToDay(String yyyymm, int week, String pattern) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        int new_yy = Integer.parseInt(yyyymm.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymm.substring(4, 6));
        int new_dd = 1;
        cal.set(new_yy, new_mm - 1, new_dd);
        // 임시 코드
        if (cal.get(cal.DAY_OF_WEEK) == cal.SUNDAY) {
            week = week - 1;
        }
        cal.add(Calendar.DATE, (week - 1) * 7 + (cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK)));
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.FRANCE);
        return formatter.format(cal.getTime());
    }
    public static String getOpDate(int field, int amount, String date) {
        GregorianCalendar calDate = getGregorianCalendar(date);
        if (field == Calendar.YEAR) {
            calDate.add(GregorianCalendar.YEAR, amount);
        } else if (field == Calendar.MONTH) {
            calDate.add(GregorianCalendar.MONTH, amount);
        } else {
            calDate.add(GregorianCalendar.DATE, amount);
        }
        return getYyyymmdd(calDate);
    }
    public static int getWeek(String yyyymmdd, int addDay) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));
        cal.set(new_yy, new_mm - 1, new_dd);
        cal.add(Calendar.DATE, addDay);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        return week;
    }
    /**
     * 예제) 
     *      DateUtil.getLastDayOfMon(2017, 2);
     *      DateUtil.getLastDayOfMon("2017", "02");
     *      DateUtil.getLastDayOfMon("201702");
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getLastDayOfMon(String yyyymm) {
        int year = Integer.parseInt(yyyymm.substring(0, 4));
        int month = Integer.parseInt(yyyymm.substring(4));
        return getLastDayOfMon(year,month);
    }
    public static int getLastDayOfMon(String yearStr, String monthStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        return getLastDayOfMon(year,month);
    }
    public static int getLastDayOfMon(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
   
    public static boolean isCorrect(String yyyymmdd) {
        boolean flag = false;
        if (yyyymmdd.length() < 8)
            return false;
        try {
            int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
            int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
            int dd = Integer.parseInt(yyyymmdd.substring(6));
            flag = DateUtil.isCorrect(yyyy, mm, dd);
        } catch (Exception ex) {
            return false;
        }
        return flag;
    }
    public static boolean isCorrect(int yyyy, int mm, int dd) {
        if (yyyy < 0 || mm < 0 || dd < 0)
            return false;
        if (mm > 12 || dd > 31)
            return false;
        String year = "" + yyyy;
        String month = "00" + mm;
        String year_str = year + month.substring(month.length() - 2);
        int endday = DateUtil.getLastDayOfMon(year_str);
        if (dd > endday)
            return false;
        return true;
    }
    public static String getThisDay(String type) {
        Date date = new Date();
        SimpleDateFormat sdf = null;
        try {
            if (type.toLowerCase().equals(YYYYMMDD.toLowerCase())) {
                sdf = new SimpleDateFormat(YYYYMMDD);
                return sdf.format(date);
            }
            if (type.toLowerCase().equals(YYYYMMDDHH.toLowerCase())) {
                sdf = new SimpleDateFormat(YYYYMMDDHH);
                return sdf.format(date);
            }
            if (type.toLowerCase().equals(YYYYMMDDHHMM.toLowerCase())) {
                sdf = new SimpleDateFormat(YYYYMMDDHHMM);
                return sdf.format(date);
            }
            if (type.toLowerCase().equals(YYYYMMDDHHMMSS.toLowerCase())) {
                sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
                return sdf.format(date);
            }
            if (type.toLowerCase().equals(YYYYMMDD_HHMMSS.toLowerCase())) {
                sdf = new SimpleDateFormat(YYYYMMDD_HHMMSS);
                return sdf.format(date);
            }
            if (type.toLowerCase().equals(YYYYMMDDHHMMSSSSS.toLowerCase())) {
                sdf = new SimpleDateFormat(YYYYMMDDHHMMSSSSS);
                return sdf.format(date);
            } else {
                sdf = new SimpleDateFormat(type);
                return sdf.format(date);
            }
        } catch (Exception e) {
            return "[ ERROR ]: parameter must be 'YYYYMMDD', 'YYYYMMDDHH', 'YYYYMMDDHHSS'or 'YYYYMMDDHHSSMS'";
        }
    }
    public static String changeDateFormat(String yyyymmdd) {
        String rtnDate = null;
        String yyyy = yyyymmdd.substring(0, 4);
        String mm = yyyymmdd.substring(4, 6);
        String dd = yyyymmdd.substring(6, 8);
        rtnDate = yyyy + " 년 " + mm + " 월 " + dd + " 일";
        return rtnDate;
    }
    /**
     * String Date Format : yyyymmdd
     * 
     * return   X     means startDate < endDate And difference Days : X Days
     * return   0     means startDate == endDate And difference Days : 0 Days
     * return   -X    means startDate > endDate And difference Days : X Days
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDifferDays(String startDate, String endDate) {
        GregorianCalendar StartDate = getGregorianCalendar(startDate);
        GregorianCalendar EndDate = getGregorianCalendar(endDate);
        long difer = (EndDate.getTime().getTime() - StartDate.getTime().getTime()) / 86400000;
        return difer;
    }
    /**
     * 
     * return   X     means startDate < endDate And difference Days : X Days
     * return   0     means startDate == endDate And difference Days : 0 Days
     * return   -X    means startDate > endDate And difference Days : X Days
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDifferDays(Date startDate, Date endDate) {
        return getDifferDays(dateToString(startDate, YYYYMMDD), dateToString(endDate, YYYYMMDD));
    }
    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }
    public static int getWeekOfYear() {
        Locale LOCALE_COUNTRY = Locale.KOREA;
        Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
        int week_of_year = rightNow.get(Calendar.WEEK_OF_YEAR);
        return week_of_year;
    }
    public static int getWeekOfMonth() {
        Locale LOCALE_COUNTRY = Locale.KOREA;
        Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
        int week_of_month = rightNow.get(Calendar.WEEK_OF_MONTH);
        return week_of_month;
    }
    public static Calendar getCalendarInstance(String p_date) {
        // Locale LOCALE_COUNTRY = Locale.KOREA;
        Locale LOCALE_COUNTRY = Locale.FRANCE;
        Calendar retCal = Calendar.getInstance(LOCALE_COUNTRY);
        if (p_date != null && p_date.length() == 8) {
            int year = Integer.parseInt(p_date.substring(0, 4));
            int month = Integer.parseInt(p_date.substring(4, 6)) - 1;
            int date = Integer.parseInt(p_date.substring(6));
            retCal.set(year, month, date);
        }
        return retCal;
    }
    public static Date getDate(int year, int month, boolean truncate) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        if (truncate) {
            cal = DateUtils.truncate(cal, Calendar.MONTH);
        }
        return cal.getTime();
    }
    public static Date getDate(int year, int month) {
        return getDate(year, month, false);
    }
    /**
     * 1년 후의 날을 구한다.
     */
    public static Date getNextYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }
    /**
     * 1년 전의 날을 구한다.
     */
    public static Date getPreviousYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }
    /**
     * 한달 후의 날을 구한다.
     */
    public static Date getNextMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
    /**
     * 한달 전의 날을 구한다.
     */
    public static Date getPreviousMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }
    /**
     * 7일 후의 날을 구한다.
     */
    public static Date getNextWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }
    /**
     * 7일전의 날을 구한다.
     */
    public static Date getPreviousWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }
    /**
     * 다음 날을 구한다.
     */
    public static Date getNextDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }
    /**
     * 하루 전 날을 구한다.
     */
    public static Date getPreviousDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    /**
     * 해당 주의 첫번째 날을 구한다.
     */
    public static Date getFirstDateOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, (dayOfWeek - 1) * -1);
        return cal.getTime();
    }
    /**
     * 해당 주의 마지막 날을 구한다.
     */
    public static Date getLastDateOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 7 - dayOfWeek);
        return cal.getTime();
    }
    /**
     * 해당 연도 달의 첫번째 날을 구한다.
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    /**
     * 해당 연도 달의 첫번째 날을 구한다.
     */
    public static Date getFirstDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return minimized(cal.getTime());
    }
    /**
     * 해당 연도 달의 마지막 날을 구한다.
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    /**
     * 해당 연도 달의 마지막 날을 구한다.
     */
    public static Date getLastDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return maximize(cal.getTime());
    }
    /**
     * 시,분,초를 모두 최소치로 초기화한다.
     */
    public static Date minimized(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }
    /**
     * 시,분,초를 모두 최대치로 초기화한다.
     */
    public static Date maximize(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    public static void main(String[] args) {
        try {
            String year = "2017";
            String month = "2";
            logger.error("DateUtil.getLastDayOfMon(2017, 2);       : {}",DateUtil.getLastDayOfMon(2017, 2)        );
            logger.error("DateUtil.getLastDayOfMon(\"2017\", \"02\");  : {}",DateUtil.getLastDayOfMon("2017", "02")   );
            logger.error("DateUtil.getLastDayOfMon(\"201702\");      : {}",DateUtil.getLastDayOfMon("201702")       );
            logger.error("getDateStrAMPM : "+getDateStrAMPM(new Date()));
            logger.error("getDateStrAMPM_Korean : "+getDateStrAMPM_Korean(new Date()));
//            Date prevStartDate = DateUtil.stringToDate("2016-05-15", DateUtil.YYYY_MM_DD);
//            Date newStartDate = DateUtil.stringToDate("2016-05-20", DateUtil.YYYY_MM_DD);
//            logger.error(DateUtil.getDifferDays(prevStartDate, newStartDate));
//            logger.error(DateUtil.getDifferDays(newStartDate, prevStartDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
