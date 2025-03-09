package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.*;

import com.parkit.parkingsystem.util.DateUtil;
import org.junit.jupiter.api.Test;
import java.util.Date;

class DateUtilTest {

    @Test
    void testGetDuration_ValidDates() {
        Date start = new Date();
        Date end = new Date(start.getTime() + 10000); // +10s

        assertEquals(10000, DateUtil.getDuration(start, end));
    }

    @Test
    void testGetDuration_InvalidDates() {
        Date start = new Date();
        Date end = new Date(start.getTime() - 10000); // -10s

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.getDuration(start, end);
        });

        assertEquals("Invalid dates", exception.getMessage());
    }
}
