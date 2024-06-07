package com.github.lamico.gui.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;

public class DateUtil {
    private DateUtil() {
    }

    public static LocalDate sqlDateToLocalDate(java.util.Date date) {
        return date != null ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    public static Date localDateToSQLDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }
}