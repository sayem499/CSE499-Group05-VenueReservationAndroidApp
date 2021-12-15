package com.reservation.app.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Fatema Kishwar
 * @since 12/15/21
 */
public class DateTimeUtils {

    private static final String DATE_TO_STR_PATTERN = "d MMM yyyy";

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    public static String dateToStr(Date date) {
        DateFormat dateFormat = new DateFormat();

        return dateFormat.format(DATE_TO_STR_PATTERN, date).toString();
    }
}
