/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.util.db.sqlserver;

import com.service.util.Utility;
import com.service.util.TimeUtil;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Karoons
 */
public class Crud extends Utility{


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
