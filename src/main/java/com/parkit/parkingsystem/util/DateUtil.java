package com.parkit.parkingsystem.util;

import java.util.Date;

public class DateUtil {
    public static long getDuration(Date start, Date end) {
        if (start == null || end == null || end.before(start)) {
            throw new IllegalArgumentException("Invalid dates");
        }
        return end.getTime() - start.getTime();
    }
}
