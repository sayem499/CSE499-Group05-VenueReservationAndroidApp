package com.reservation.app.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Fatema Kishwar
 * @since 12/15/21
 */
public class DateTimeUtils {

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }
}
