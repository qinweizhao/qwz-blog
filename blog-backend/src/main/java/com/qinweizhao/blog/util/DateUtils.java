package com.qinweizhao.blog.util;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Date utilities.
 *
 * @since 3/18/19
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * Gets current date.
     *
     * @return current date
     */
    @NonNull
    public static Date now() {
        return new Date();
    }


    /**
     * Adds date.
     *
     * @param date     current date must not be null
     * @param time     time must not be less than 1
     * @param timeUnit time unit must not be null
     * @return added date
     */
    public static Date add(@NonNull Date date, long time, @NonNull TimeUnit timeUnit) {
        Assert.notNull(date, "Date must not be null");
        Assert.isTrue(time >= 0, "Addition time must not be less than 1");
        Assert.notNull(timeUnit, "Time unit must not be null");

        Date result;

        int timeIntValue;

        if (time > Integer.MAX_VALUE) {
            timeIntValue = Integer.MAX_VALUE;
        } else {
            timeIntValue = (int) time;
        }

        // Calc the expiry time
        switch (timeUnit) {
            case DAYS:
                result = org.apache.commons.lang3.time.DateUtils.addDays(date, timeIntValue);
                break;
            case HOURS:
                result = org.apache.commons.lang3.time.DateUtils.addHours(date, timeIntValue);
                break;
            case MINUTES:
                result = org.apache.commons.lang3.time.DateUtils.addMinutes(date, timeIntValue);
                break;
            case SECONDS:
                result = org.apache.commons.lang3.time.DateUtils.addSeconds(date, timeIntValue);
                break;
            case MILLISECONDS:
                result = org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, timeIntValue);
                break;
            default:
                result = date;
        }
        return result;
    }
}
