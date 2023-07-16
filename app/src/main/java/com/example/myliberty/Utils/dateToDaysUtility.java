package com.example.myliberty.Utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class dateToDaysUtility {
    public static Long calculateDifference(String cycleEndDate, String cycleStartDate, String value) {
        Timestamp date_1 = stringToTimestamp(cycleEndDate);
        Timestamp date_2 = stringToTimestamp(cycleStartDate);
        long milliseconds = Math.abs(date_2.getTime() - date_1.getTime());
        if (value.equals("second"))
            return milliseconds / 1000;
        if (value.equals("minute"))
            return milliseconds / 1000 / 60;
        if (value.equals("hours"))
            return milliseconds / 1000 / 3600;
        if (value.equals("days"))
            return milliseconds / 1000 / 3600/24;

        return 99999L;
    }
    public static Long calculateDifference(String cycleStartDate, String value) {
        Timestamp date_1 = new Timestamp(System.currentTimeMillis());
        Timestamp date_2 = stringToTimestamp(cycleStartDate);
        long milliseconds = Math.abs(date_1.getTime() - date_2.getTime());

        if (value.equals("second"))
            return milliseconds / 1000;
        if (value.equals("minute"))
            return milliseconds / 1000 / 60;
        if (value.equals("hours"))
            return milliseconds / 1000 / 3600;
        if (value.equals("days"))
            return milliseconds / 1000 / 3600/24;

        return 99999L;
    }
    public static Timestamp stringToTimestamp(String date) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(date);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }
    public static Timestamp addDays(Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);// w ww.  j ava  2  s  .co m
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new Timestamp(cal.getTime().getTime());

    }

}
