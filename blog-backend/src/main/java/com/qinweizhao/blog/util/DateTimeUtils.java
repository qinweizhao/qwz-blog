package com.qinweizhao.blog.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具
 *
 * @author LeiXinXin
 * @since 2022-03-15
 */
public class DateTimeUtils {

    /**
     * 上海时区格式
     */
    public static final String CTT = ZoneId.SHORT_IDS.get("CTT");

    /**
     * 上海时区
     */
    public static final ZoneId CTT_ZONE_ID = ZoneId.of(CTT);


    private DateTimeUtils() {
    }

    /**
     * 获取当前时间，默认为上海时区
     *
     * @return Now LocalDateTime
     */
    public static LocalDateTime now() {
        return now(CTT_ZONE_ID);
    }

    /**
     * 根据时区获取当前时间
     *
     * @param zoneId 时区
     * @return Now LocalDateTime
     */
    public static LocalDateTime now(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }


    /**
     * 根据日期格式化时间
     *
     * @param localDateTime 时间
     * @param formatter     时间格式
     * @return Result
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    /**
     * 根据时间格式，格式化时间
     *
     * @param localTime 时间
     * @param formatter 时间格式
     * @return Result
     */
    public static String format(LocalTime localTime, DateTimeFormatter formatter) {
        return localTime.format(formatter);
    }

    /**
     * 根据日期格式，格式化日期
     *
     * @param localDate 日期
     * @param formatter 日期格式
     * @return Result
     */
    public static String format(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.format(formatter);
    }


    /**
     * to instant by default zoneId(Shanghai)
     *
     * @param localDateTime 时间
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        return toInstant(localDateTime, CTT_ZONE_ID);
    }

    /**
     * To instant by zoneId
     *
     * @param localDateTime 时间
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant();
    }

    /**
     * 将localDateTime 转为时间戳
     *
     * @param localDateTime 时间
     * @return 时间戳
     */
    public static long toEpochMilli(LocalDateTime localDateTime) {
        return toInstant(localDateTime).toEpochMilli();
    }

}
