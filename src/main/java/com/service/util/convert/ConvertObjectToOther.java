/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util.convert;

import com.service.util.Utility;
import com.service.util.annotations.DomainAnnotation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Karoons
 */
public class ConvertObjectToOther extends Utility {

    public <T> Map<String, Object> convertObjectToMap(T source, String fixFields) throws Exception {
        try {
            TimeUtil mpTimeUtil = new TimeUtil();
            Map<String, Object> result = null;
            result = new HashMap<>();
            for (Field field : source.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (fixFields.trim().length() != 0 && fixFields.trim().contains(field.getName())) {
                    if (fixFields.trim().contains(field.getName())) {
                        if (field.get(source) != null) {
                            if (field.getType().equals(Date.class)) {
                                result.put(field.getName(), mpTimeUtil.convertDateToStringUS((Date) field.get(source)));
                            } else {
                                result.put(field.getName(), String.valueOf(field.get(source)));
                            }
                        } else {
                            result.put(field.getName(), String.valueOf(field.get(source)));
                        }
                    } else {
                        throw new Exception("Error this " + fixFields + " not exit ");
                    }

                } else if (fixFields.trim().length() == 0) {
                    if (field.getType().equals(Date.class)) {
                        result.put(field.getName(), mpTimeUtil.convertDateToStringUS((Date) field.get(source)));
                    } else {
                        result.put(field.getName(), String.valueOf(field.get(source)));
                    }
                } else {
                }

            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> String convertObjectToStringCsv(Class<T> classTarget, List<T> sources) throws Exception {
        try {
            TimeUtil mpTimeUtil = new TimeUtil();
            Map<String, Object> result = null;
            result = new HashMap<>();

            StringBuilder output = new StringBuilder();

            StringBuilder columnName = new StringBuilder();

            // set first row
            for (Field declaredField : classTarget.newInstance().getClass().getDeclaredFields()) {

                String fieldName = "";
                if (this.allowToConvert(declaredField)) {
                    fieldName = this.getFielName(declaredField);
                    if (columnName.length() == 0) {
                        columnName.append("").append(fieldName);
                    } else {
                        columnName.append(",").append(fieldName);
                    }
                }
            }

            output.append(columnName);
            output.append(System.getProperty("line.separator"));

            StringBuilder rowValue = new StringBuilder();
            for (T source : sources) {
                Integer rowIndex = 0;
                for (Field field : source.getClass().getDeclaredFields()) {
                    if (this.allowToConvert(field)) {
                        String _value = "";
                        field.setAccessible(true);
                        if (field.get(source) != null) {
                            if (field.getType().equals(Date.class)) {
                                _value = mpTimeUtil.convertDateToStringUS((Date) field.get(source));
                            } else {
                                _value = String.valueOf(field.get(source));
                            }
                        }

                        if (rowIndex == 0) {
                            rowValue.append("").append(_value);
                            rowIndex = 1;
                        } else {
                            rowValue.append(",").append(_value);
                        }
                    }
                }
                rowValue.append(System.getProperty("line.separator"));
            }

            output.append(rowValue);

            return output.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> String convertObjectToStringCsv(Class<T> classTarget, List<T> sources, Boolean renderColumnName) throws Exception {
        try {
            TimeUtil mpTimeUtil = new TimeUtil();
            Map<String, Object> result = null;
            result = new HashMap<>();

            StringBuilder output = new StringBuilder();
            if (renderColumnName) {
                StringBuilder columnName = new StringBuilder();
                // set first row
                for (Field declaredField : classTarget.newInstance().getClass().getDeclaredFields()) {

                    String fieldName = "";
                    Annotation annotation = declaredField.getAnnotation(DomainAnnotation.class);
                    if (null != annotation) {
                        if (annotation instanceof DomainAnnotation) {
                            DomainAnnotation myAnnotation = (DomainAnnotation) annotation;
                            fieldName = myAnnotation.dbFieldName();
                        }
                    } else {
                        fieldName = declaredField.getName();
                    }

                    if (columnName.length() == 0) {
                        columnName.append("").append(fieldName);
                    } else {
                        columnName.append(",").append(fieldName);
                    }
                }

                output.append(columnName);
                output.append(System.getProperty("line.separator"));
            }

            StringBuilder rowValue = new StringBuilder();
            for (T source : sources) {
                Integer rowIndex = 0;
                for (Field field : source.getClass().getDeclaredFields()) {
                    String _value = "";
                    field.setAccessible(true);
                    if (field.get(source) != null) {
                        if (field.getType().equals(Date.class)) {
                            _value = mpTimeUtil.convertDateToStringUS((Date) field.get(source));
                        } else {
                            if (field.get(source) != null) {
                                _value = String.valueOf(field.get(source));
                            } else {
                                _value = "";
                            }
//                            System.out.println("-- " + field.getName() + "-- " + field.getType() + "--" + _value);
                        }
                    } else {
//                        System.out.println("-- " + field.getName() + "-- " + field.getType());
                        _value = String.valueOf(field.get(source));
                    }

                    if (rowIndex == 0) {
                        rowValue.append("").append(_value);
                        rowIndex = 1;
                    } else {
                        rowValue.append(",").append(_value);
                    }
                }
                rowValue.append(System.getProperty("line.separator"));
            }

            output.append(rowValue);
//            System.out.println(rowValue);

            return output.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}
