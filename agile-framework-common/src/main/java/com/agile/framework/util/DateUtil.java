package com.agile.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {
    private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public enum Field {
        YEAR,
        MONTH,
        WEEK,
        DAY,
        HOUR,   // 24小时制
        MINUTE,
        SECOND,
        MILLISECOND
    }

    public static String format(Date date) {
    	try {
            return format(date, yyyy_MM_dd_HH_mm_ss);
    	} catch (Exception e) {
            log.error(e.getMessage(), e);
    		throw new RuntimeException(e);
    	}
    }

    public static Date parse(String strDate) {
    	try {
            return parse(strDate, yyyy_MM_dd_HH_mm_ss);
    	} catch (Exception e) {
            log.error(e.getMessage(), e);
    		throw new RuntimeException(e);
    	}
    }
    
    public static String format(Date date, String format) {
    	try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
    	} catch (Exception e) {
            log.error(e.getMessage(), e);
    		throw new RuntimeException(e);
    	}
    }
    
    public static Date parse(String strDate, String format) {
    	try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(strDate);
    	} catch (Exception e) {
            log.error(e.getMessage(), e);
    		throw new RuntimeException(e);
    	}
    }

    /**
     * Date转换为LocalDateTime
     * @param date
     */
    public static LocalDateTime date2LocalDateTime(Date date){
    	return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
 
    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
    	return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * String转换为LocalDate
     * @param date
     */
    public static LocalDate stringToLocalDate(String date, String pattern){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern == null ? "yyyy-MM-dd" : pattern);
        return LocalDate.parse(date, df);
    }


    /**
     * 获取两个日期之间的所有月份 (年月)
     * @param startTime 开始时间, 必须 yyyy-MM 开头
     * @param endTime 结束时间, 必须 yyyy-MM 开头
     * @param format 返回的数据格式
     */
    public static List<String> getMonthsBetween(String startTime, String endTime, String format) {
        // 格式化参数
        startTime = startTime.substring(0, 7);
        endTime = endTime.substring(0, 7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter returnFormatter = DateTimeFormatter.ofPattern(format);
        YearMonth startMonth = YearMonth.parse(startTime, formatter);
        YearMonth endMonth = YearMonth.parse(endTime, formatter);

        List<String> monthsBetween = new ArrayList<>();
        YearMonth currentMonth = startMonth;
        // 遍历日期范围
        while (!currentMonth.isAfter(endMonth)) {
            monthsBetween.add(currentMonth.format(returnFormatter));
            currentMonth = currentMonth.plusMonths(1);
        }
        return monthsBetween;
    }

    /**
     * 字符串日期间隔差，返回指定域的时间差绝对值
     * @param dateStr1
     * @param dateStr2
     * @param field
     * @return int
     * */
    public static long between(String dateStr1, String dateStr2, String format, Field field){
        if (StringUtils.isEmpty(dateStr1) && StringUtils.isEmpty(dateStr2)) {
            return 0;
        }
        if (StringUtils.isEmpty(format)) {
            format = yyyy_MM_dd_HH_mm_ss;
        }
        Date date1 = parse(dateStr1, format);
        Date date2 = parse(dateStr2, format);
        return between(date1, date2, field);
    }

    /**
     * 日期间隔差，返回指定域的时间差绝对值
     * 注意：这里是做转换成Time后的值比较，不是按域比较。如：
     * 1. 小于24小时，日、周、月、年值为0
     * 2. 大于等于24小时，小于48小时，日值为1，周、月、年值为0
     * @param date1
     * @param date2
     * @param field
     * @return int
     * */
    public static long between(Date date1, Date date2, Field field) {
        if (null == date1 && null == date2) {
            return 0;
        }
        if (null == date1) {
            date1 = new Date();
        }
        if (null == date2) {
            date2 = new Date();
        }
        long millSeconds = Math.abs(date1.getTime() - date2.getTime());
        switch (field) {
            case SECOND:
                return millSeconds / 1000;
            case MINUTE:
                return millSeconds / 1000 / 60;
            case HOUR:
                return millSeconds / 1000 / 3600;
            case DAY:
                return millSeconds / 1000 / 3600 / 24;
            case WEEK:
                return millSeconds / 1000 / 3600 / 24 / 7;
            case MONTH:
                return millSeconds / 1000 / 3600 /24 / 30;
            case YEAR:
                return millSeconds / 1000 / 3600 / 24/ 365;
        }
        return millSeconds;
    }

    // 昨日
    public List<String> getYesterday() {
        String yesterday = LocalDate.now().minusDays(1).toString();
        return Collections.singletonList(yesterday);
    }

    // 本周 七天
    public List<String> getCurrentWeekDays() {
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.ISO;
        // 周一
        LocalDate startDate = now.with(weekFields.dayOfWeek(), 1L);
        // 周日
        LocalDate endDate = now.with(weekFields.dayOfWeek(), 7L);
        return getAscDateList(startDate, endDate);
    }

    // 本月
    public List<String> getCurrentMonthDays() {
        LocalDate now = LocalDate.now();
        // 本月1号
        LocalDate firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        // 本月最后一天
        LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
        return getAscDateList(firstDayOfMonth, lastDayOfMonth);
    }

    // 本季度
    public List<String> getCurrentQuarterDays() {
        // 当前季度第一天
        LocalDate startOrStartDayOfQuarter = getStartOrEndDayOfQuarter(true);
        // 当前季度最后一天
        LocalDate startOrEndDayOfQuarter = getStartOrEndDayOfQuarter(false);
        return getAscDateList(startOrStartDayOfQuarter, startOrEndDayOfQuarter);
    }

    // 近几天
    public List<String> getCustomDays(Integer nearDays) {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().minusDays(nearDays);
        return getAscDateList(endDate, startDate);
    }

    // 固定时间段
    public List<String> getFixedDays(LocalDate startDate, LocalDate endDate) {
        return getAscDateList(startDate, endDate);
    }

    /**
     * 获取 两个时间内 全部日期
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link List}<{@link String}>
     */
    public static List<String> getAscDateList(LocalDate startDate, LocalDate endDate) {
        List<String> result = new ArrayList<>();
        if (endDate.compareTo(startDate) < 0) {
            return result;
        }
        while (true) {
            result.add(startDate.toString());
            if (startDate.compareTo(endDate) >= 0) {
                break;
            }
            startDate = startDate.plusDays(1);
        }
        return result;
    }

    /**
     * 季度开始或结束一天
     *
     * @param isFirst true 季度开始 false 季度结束
     * @return {@link LocalDate}
     */
    public static LocalDate getStartOrEndDayOfQuarter(Boolean isFirst) {
        LocalDate today = LocalDate.now();
        LocalDate resDate;
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
        }
        return resDate;
    }

    /**
     * 获取前时间后一小时的时间
     * @param date
     * @return java.util.Date
     */
    public static Date afterOneHourDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime();
    }

    /**
     * 获取某一日期的前/后N天日期
     * @param dateStr 日期字符串
     * @param format 日期格式
     * @return 与入参相同格式的相距日期
     */
    public static String intervalDate(String dateStr, String format, int n) {
        Date date = parse(dateStr, format);
        return format(intervalDate(date, n), format);
    }

    /**
     * 获取某一日期的前/后N天日期
     * @param date 指定日期
     * @param n 相距日期，负数则为之前，-1（昨天）, 1（明天）
     * @return java.util.Date
     */
    public static Date intervalDate(Date date, int n) {
        // 获取 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        // 返回相距日期
        return calendar.getTime();
    }
}