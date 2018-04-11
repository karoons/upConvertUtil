/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util.convert;

import com.service.util.Utility;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Karoons
 */
public class ConvertMapToOther extends Utility {
     public <T> Object convertMapToObject(Class<T> classTarget, Map<String, Object> source) throws Exception {
        try {

            T result = classTarget.newInstance();

            TimeUtil mpTimeUtil = new TimeUtil();

            for (Map.Entry<String, Object> entry : source.entrySet()) {
                Field field = null;
                try {
                    field = result.getClass().getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    if (field.getType().equals(entry.getValue().getClass())) {
                        field.set(result, entry.getValue());
                    } else {
                        String trackingMessage = "Convert " + entry.getKey()
                                + "[" + entry.getValue().getClass() + ":" + entry.getValue()
                                + "] to " + field.getName() + "[" + field.getType() + "]";
                        if (field.getType().equals(Date.class)) {
                            Date _date = null;
                            String specFormat = (this.getSpecType(field) != null) ? (String) this.getSpecType(field) : "";
                            if (!specFormat.isEmpty()) {
                                _date = mpTimeUtil.convertStringToDate((String) entry.getValue(), specFormat);
                            } else {
                                _date = mpTimeUtil.convertStringToDate((String) entry.getValue(), "yyyy-MM-dd HH:mm");
                            }
                            field.set(result, _date);
                        } else if (field.getType().equals(Integer.class)) {
                            // Integer
                            field.set(result, Integer.valueOf((String) entry.getValue()));
                        } else if (field.getType().equals(String.class)) {
                            //String
                            field.set(result, String.valueOf(entry.getValue()));
                        } else if (field.getType().equals(BigDecimal.class)) {
                            //BigDecimal
                            field.set(result, BigDecimal.valueOf(Double.valueOf(String.valueOf(entry.getValue()))));
                        } else if (field.getType().equals(Boolean.class)) {
                            //BigDecimal
                            field.set(result, Boolean.valueOf(String.valueOf(entry.getValue())));
                        } else {
                            trackingMessage += " this fuction not support this data type";
                            throw new Exception(trackingMessage);
                        }
                    }
                } catch (NoSuchFieldException e) {
//                    System.out.println("NoSuchFieldException : " + e.getMessage());
                }

            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
}
