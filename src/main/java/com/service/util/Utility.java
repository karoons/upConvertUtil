package com.service.util;

import com.service.util.annotations.ClassDomainAnnotation;
import com.service.util.annotations.DomainAnnotation;
import com.service.util.annotations.SpecDataTypeAnnotation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Karoons
 */
public class Utility {

    public String getFielName(Field field) throws Exception {
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

    public <T> String getTableName(T insClass) throws Exception {
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

    public String getFieldName(Field field) throws Exception {
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

    public String escapeHtml(String param) {
        return param.replaceAll("'", "''");
    }

    public Object getSpecType(Field field) throws Exception {
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

    public Boolean allowToConvert(Field field) throws Exception {
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
}
