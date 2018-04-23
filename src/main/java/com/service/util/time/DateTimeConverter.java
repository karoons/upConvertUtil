/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util.time;

import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author Karoons
 */
public class DateTimeConverter {

    public String convertToString(String outputFormat, Object source) throws Exception {
        try {
            /*
                The source object is Date and Timestamp value
            */
            return new DateTime(source).toString(outputFormat, Locale.ENGLISH);
        } catch (Exception e) {
            throw e;
        }
    }

    public Date convertStringToDate(String outputFormat, String source) throws Exception {
        try {
            return DateTime.parse(source, DateTimeFormat.forPattern(outputFormat).withLocale(Locale.ENGLISH)).toDate();
        } catch (Exception e) {
            throw e;
        }
    }

    public Date getCurrentDateTime() throws Exception {
        try {
            return DateTime.now().toDate();
        } catch (Exception e) {
            throw e;
        }
    }

}
