/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util;

import com.service.util.time.DateTimeConverter;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Karoons
 */
public class Hello {

    public static void main(String[] args) throws Exception {
        DateTimeConverter dt = new DateTimeConverter();
//        System.out.println("------------------- " + dt.convertToString("FF", new Date()));
        System.out.println("------------------- " + dt.convertToString("yyyy-MM-dd HH:mm:ss", new Timestamp(new Date().getTime())));
//        System.out.println("------------------- " + dt.convertToString("yyyy.MM.dd", new Date()));
//        System.out.println("------------------- " + dt.convertStringToDate("yyyy.MM.dd HH:mm", "2018.01.30 16:49"));

    }
}
