/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Karoons
 */
public class TimeUtil {

//    @Value("${myproject.dateFormat}")
    private String displayDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private String displayDateFormat = "dd/MM/yyyy";

    public String convertTimestampToStringUS(Timestamp value) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(displayDateTimeFormat, Locale.ENGLISH);
            return dateFormat.format(value);
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertDateToStringUS(Date value) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(displayDateTimeFormat, Locale.ENGLISH);
            return dateFormat.format(value);
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertDateToStringUS(Date value, String displayFormat) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(displayFormat, Locale.ENGLISH);
            return dateFormat.format(value);
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertTimestampToStringUS(Timestamp value, String displayFormat) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(displayFormat, Locale.US);
            return dateFormat.format(value);
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertDateToStringUSForSAP(Date value) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            return dateFormat.format(value);
        } catch (Exception e) {
            throw e;
        }
    }

    public Date convertStringToDate(String param) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(displayDateTimeFormat, Locale.ENGLISH);
            return dateFormat.parse(param);
        } catch (Exception e) {
            throw e;
        }
    }

    public Date convertStringToDate(String param, String format) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
            return dateFormat.parse(param);
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertStringToStringOnFormatDate(String param) throws Exception {
        try {
            DateFormat dateFormatSap = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            SimpleDateFormat dateFormat = new SimpleDateFormat(displayDateTimeFormat, Locale.ENGLISH);
            return dateFormat.format(dateFormatSap.parse(param));
        } catch (Exception e) {
            throw e;
        }
    }

    public String convertStringToStringOnFormatDate(String sourceFormat, String destinationFormat, String param) throws Exception {
        try {
            DateFormat dateFormatSap = new SimpleDateFormat(sourceFormat, Locale.ENGLISH);
            SimpleDateFormat dateFormat = new SimpleDateFormat(destinationFormat, Locale.ENGLISH);
            return dateFormat.format(dateFormatSap.parse(param));
        } catch (Exception e) {
            throw e;
        }
    }

    public String getCurrentDateOnTextFormat() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(displayDateFormat, Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }

    public String getCurrentDateOnTextFormat(String fixFormat) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(fixFormat, Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }

    public String getCurrentYearOnTextFormat() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }

    public String getCurrentYearOnTextThai2LastDigitFormat() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String year = "" + ((Integer.parseInt(dateFormat.format(currentDate)) + 543));
        return year.substring(2, 4);
    }

    public Date yesterday() {
        Locale locale1 = Locale.ENGLISH;
        final Calendar cal = Calendar.getInstance(locale1);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getDate(Integer hour, Integer minute, Integer currentDay) {
        /*
            Day +1
         */
        Locale locale1 = Locale.ENGLISH;
        final Calendar cal = Calendar.getInstance(locale1);
        cal.add(Calendar.DATE, currentDay);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date datePlus(Date originalDate, Integer minute) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(originalDate);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    public int getDateDiff(Date startDate, Date endDate) throws Exception {
        try {
            return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            throw e;
        }
    }

    public Integer getNumberOfMinuteBetweenDate(Date startDate, Date endDate) throws Exception {
        try {
            Integer days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60));
//            Integer hr = days * 24;
//            Integer minure = hr * 60;
            return days;
        } catch (Exception e) {
            throw e;
        }
    }


}
