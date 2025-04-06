package com.parkit.parkingsystem.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtil {

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
