/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util.convert;

import com.service.util.annotations.ClassDomainAnnotation;
import com.service.util.annotations.DomainAnnotation;
import com.service.util.annotations.SpecDataTypeAnnotation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Karoons
 */
public class ObjectConvertUtil {

    private Boolean allowToConvert(Field field) throws Exception {
        try {
            Boolean allowToCreate = true;
            Annotation annotation = field.getAnnotation(DomainAnnotation.class);
            if (null != annotation) {
                if (annotation instanceof DomainAnnotation) {
                    DomainAnnotation myAnnotation = (DomainAnnotation) annotation;
                    allowToCreate = myAnnotation.dbAllowToCovertToCsv();
                }
            }
            return allowToCreate;
        } catch (Exception e) {
            throw e;
        }
    }

    private Object getSpecType(Field field) throws Exception {
        try {
            Object result = "";
            Boolean allowToCreate = true;
            Annotation annotation = field.getAnnotation(SpecDataTypeAnnotation.class);
            if (null != annotation) {
                if (annotation instanceof SpecDataTypeAnnotation) {
                    SpecDataTypeAnnotation myAnnotation = (SpecDataTypeAnnotation) annotation;
                    if (myAnnotation.isSapDate()) {
                        result = myAnnotation.format();
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    private String getFielName(Field field) throws Exception {
        try {
            String fieldName = "";
            Annotation annotation = field.getAnnotation(DomainAnnotation.class);
            if (null != annotation) {
                if (annotation instanceof DomainAnnotation) {
                    DomainAnnotation myAnnotation = (DomainAnnotation) annotation;
                    fieldName = myAnnotation.dbFieldName();
                }
            }
            return fieldName;
        } catch (Exception e) {
            throw e;
        }
    }

    private <T> String getTableName(T insClass) throws Exception {
        try {

            String tableName = "";

            Annotation annotation = insClass.getClass().getAnnotation(ClassDomainAnnotation.class);
            if (annotation instanceof ClassDomainAnnotation) {
                ClassDomainAnnotation classDomainAnnotation = (ClassDomainAnnotation) annotation;
                tableName = classDomainAnnotation.dbName();
            }
            if (tableName == null || "".equals(tableName)) {
                throw new Exception("Repository.insert requir @ClassDomainAnnotation(dbName = \" TABLE_NAME \")");
            }

            return tableName;

        } catch (Exception e) {
            throw e;
        }
    }

    private String getFieldName(Field field) throws Exception {
        try {

            String fieldName = "";

            Annotation annotation = field.getAnnotation(DomainAnnotation.class);
            if (null != annotation) {
                if (annotation instanceof DomainAnnotation) {
                    DomainAnnotation myAnnotation = (DomainAnnotation) annotation;
                    fieldName = myAnnotation.dbFieldName();
                }
            } else {
                fieldName = field.getName();
            }

            return fieldName;
        } catch (Exception e) {
            throw e;
        }
    }

    private String escapeHtml(String param) {
        return param.replaceAll("'", "''");
    }

    public <T> Object convertMapToObject(Class<T> classTarget, Map<String, Object> source) throws Exception {
        try {

            T result = classTarget.newInstance();

            TimeUtil mpTimeUtil = new TimeUtil();

            for (Map.Entry<String, Object> entry : source.entrySet()) {
//                System.out.println("entry.getKey()--" + entry.getKey());
//                for (Field declaredField : result.getClass().getDeclaredFields()) {
//                    System.out.println("--" + declaredField.getName());
//                }
//                System.out.println("entry.getKey()--Start Check");
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

//                if (field != null) {
//
//                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

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
//                    System.out.println("-- not fix -- ");
//                    System.out.println("---- field.getName()---" + field.getName());
                    if (field.getType().equals(Date.class)) {
                        result.put(field.getName(), mpTimeUtil.convertDateToStringUS((Date) field.get(source)));
                    } else {
                        result.put(field.getName(), String.valueOf(field.get(source)));
                    }
                } else {
                    // not action
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

    public <T> String genInsertCommand(T domain, String fieldsNotGen) throws Exception {
        try {
            TimeUtil mpTimeUtil = new TimeUtil();
            String tableName = getTableName(domain);

            StringBuilder mainCommand = new StringBuilder();
            StringBuilder fieldString = new StringBuilder();
            StringBuilder valueString = new StringBuilder();

            mainCommand.append("INSERT INTO ").append(tableName).append(" ");
            for (Field field : domain.getClass().getDeclaredFields()) {
                try {
                    String fieldName = getFieldName(field);
                    field.setAccessible(true);
                    if (!fieldsNotGen.contains(fieldName)) {
                        if (field.get(domain) != null) {
                            fieldString.append(",").append(fieldName);
                            if (field.getType().equals(String.class)) {
                                valueString.append(",'").append(escapeHtml((String) field.get(domain))).append("'");
                            } else if (field.getType().equals(Boolean.class)) {
                                valueString.append(",'").append(field.get(domain)).append("'");
                            } else if (field.getType().equals(Timestamp.class)) {
                                valueString.append(",'").append(mpTimeUtil.convertTimestampToStringUS((Timestamp) field.get(domain), "yyyy-MM-dd HH:mm:ss")).append("'");
                            } else if (field.getType().equals(Date.class)) {
                                valueString.append(",'").append(mpTimeUtil.convertDateToStringUS((Date) field.get(domain), "yyyy-MM-dd HH:mm:ss")).append("'");
                            } else {
                                valueString.append(",").append(field.get(domain)).append("");
                            }
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    System.out.println("command : " + ex);
                    throw ex;
                }
            }
            mainCommand.append(" (").append(fieldString.deleteCharAt(0).toString()).append(")").append(" VALUES ").append("(").append(valueString.deleteCharAt(0).toString()).append(");");
            return mainCommand.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> String genUpdateCommand(T domain, String fieldsNotGen, String updateCondition) throws Exception {
        try {
            TimeUtil mpTimeUtil = new TimeUtil();
            String tableName = getTableName(domain);

            StringBuilder mainCommand = new StringBuilder();
            StringBuilder fieldString = new StringBuilder();
            StringBuilder valueString = new StringBuilder();

            mainCommand.append("UPDATE ").append(tableName).append(" SET");

            if (updateCondition.trim().equals("") && updateCondition.trim() == null) {
                throw new Exception("Key condition for update record on database not found! at " + domain.getClass());
            }
            for (Field field : domain.getClass().getDeclaredFields()) {
                try {
                    String fieldName = getFieldName(field);
                    field.setAccessible(true);

                    if (!fieldsNotGen.contains(field.getName())) {
                        if (field.get(domain) != null) {
                            fieldString.append(",").append(fieldName);
                            if (field.getType().equals(String.class)) {
                                fieldString.append("='").append(escapeHtml((String) field.get(domain))).append("'");
                            } else if (field.getType().equals(Boolean.class)) {
                                fieldString.append("='").append(field.get(domain)).append("'");
                            } else if (field.getType().equals(Timestamp.class)) {
                                fieldString.append("='").append(mpTimeUtil.convertTimestampToStringUS((Timestamp) field.get(domain), "yyyy-MM-dd HH:mm:ss")).append("'");
                            } else if (field.getType().equals(Date.class)) {
                                fieldString.append("='").append(mpTimeUtil.convertDateToStringUS((Date) field.get(domain), "yyyy-MM-dd HH:mm:ss")).append("'");
                            } else {
                                fieldString.append("=").append(field.get(domain)).append("");
                            }
                        }
                    }

                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw ex;
                }
            }

            mainCommand.append(" ").append(fieldString.deleteCharAt(0).toString()).append(" WHERE ").append(updateCondition);

            return mainCommand.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String genDelete() throws Exception {
        try {
            return "";
        } catch (Exception e) {
            throw e;
        }
    }

}
