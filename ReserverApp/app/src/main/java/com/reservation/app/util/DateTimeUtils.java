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

    public static boolean checkDate(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar2.setTime(date2);

        return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }
}
