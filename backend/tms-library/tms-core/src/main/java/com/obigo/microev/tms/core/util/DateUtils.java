package com.obigo.microev.tms.core.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateUtils {

    // Default patterns
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * String -> LocalDate
     * 기본 패턴 yyyy-MM-dd
     */
    public static LocalDate stringToLocalDate(String dateStr) {
        return stringToLocalDate(dateStr, DEFAULT_DATE_PATTERN);
    }

    /**
     * String -> LocalDate
     * 패턴 설정
     */
    public static LocalDate stringToLocalDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * String -> LocalDateTime
     * 기본 패턴 yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr) {
        return stringToLocalDateTime(dateTimeStr, DEFAULT_DATE_TIME_PATTERN);
    }

    /**
     * String -> LocalDateTime
     * 패턴 설정
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }


    /**
     * LocalDate -> String
     * 기본 패턴 yyyy-MM-dd
     */
    public static String localDateToString(LocalDate date) {
        return localDateToString(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * LocalDate -> String
     * 패턴 설정
     */
    public static String localDateToString(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * LocalDateTime -> String
     * 기본 패턴 yyyy-MM-dd HH:mm:ss
     */
    public static String localDateTimeToString(LocalDateTime dateTime) {
        return localDateTimeToString(dateTime, DEFAULT_DATE_TIME_PATTERN);
    }

    /**
     * LocalDateTime -> String
     * 패턴 설정
     */
    public static String localDateTimeToString(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

}
