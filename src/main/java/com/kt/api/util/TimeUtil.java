package com.kt.api.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

public class TimeUtil {

    public static java.sql.Timestamp getNow() {
        Instant instant = Instant.now();
        Timestamp now = Timestamp.from(instant);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+Timestamp.valueOf(df.format(now)));
        return Timestamp.valueOf(df.format(now));
    }
}
